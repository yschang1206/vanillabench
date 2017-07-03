package org.vanilladb.bench.server.param.tpce;

import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.sql.BigIntConstant;
import org.vanilladb.core.sql.DoubleConstant;
import org.vanilladb.core.sql.IntegerConstant;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class TradeResultParamHelper extends StoredProcedureParamHelper {
	
	// Inputs
	private long tradeId;
	private double tradePrice;
	
	// Outputs
	private double acctBal;
	private long acctId;
	private int loadUnit;
	
	// XXX: inputs that are not in the spec.
	protected long customerId;
	protected long brokerId;

	@Override
	public void prepareParameters(Object... pars) {
		if (pars.length != 4)
			throw new RuntimeException("wrong pars list");
		int idxCntr = 0;
		tradeId = (Long) pars[idxCntr++];
		customerId = (Long) pars[idxCntr++];
		acctId = (Long) pars[idxCntr++];
		brokerId = (Long) pars[idxCntr++];
	}

	@Override
	public SpResultSet createResultSet() {
		Schema sch = new Schema();
		Type statusType = Type.VARCHAR(10);

		sch.addField("acct_bal", Type.DOUBLE);
		sch.addField("acct_id", Type.BIGINT);
		sch.addField("load_unit", Type.INTEGER);
		sch.addField("status", Type.VARCHAR(10));

		SpResultRecord rec = new SpResultRecord();
		String status = isCommitted ? "committed" : "abort";
		rec.setVal("acct_bal", new DoubleConstant(acctBal));
		rec.setVal("acct_id", new BigIntConstant(acctId));
		rec.setVal("load_unit", new IntegerConstant(loadUnit));
		rec.setVal("status", new VarcharConstant(status, statusType));

		return new SpResultSet(sch, rec);
	}

	public long getCustomerId() {
		return customerId;
	}

	public long getAcctId() {
		return acctId;
	}

	public long getBrokerId() {
		return brokerId;
	}

	public long getTradeId() {
		return tradeId;
	}
	
	// Outputs

	public void setAcctBal(double acctBal) {
		this.acctBal = acctBal;
	}

	public void setAcctId(long acctId) {
		this.acctId = acctId;
	}
	
	public void setLoadUnit(int loadUnit) {
		this.loadUnit = loadUnit;
	}
}