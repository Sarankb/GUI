package com.ka.gui.persistance;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.ka.gui.persistance.interfaces.QueryPersistanceInterface;

public class QueryPersistance {
	
	public String query_Id;
	
	public SetMultimap<String, QueryPersistanceInterface> get_Query()
	{
		SetMultimap<String, QueryPersistanceInterface> result = HashMultimap.create();
		
		QueryPersistanceInterface qt = new QueryPersistanceInterface();
		qt.field_name = "MATERIAL";
		qt.seqno = "0";
		qt.field_type = "CF";
		result.put("Column", qt);
		qt = new QueryPersistanceInterface();
		qt.field_name = "AMOUNT";
		qt.seqno = "1";
		qt.field_type = "VF";
		result.put("Column", qt);
		qt = new QueryPersistanceInterface();
		qt.field_name = "QUANT";
		qt.seqno = "2";
		qt.field_type = "VF";
		result.put("Column", qt);
		qt = new QueryPersistanceInterface();
		qt.field_name = "ORG";
		qt.seqno = "3";
		qt.field_type = "CF";
		result.put("Column", qt);
		qt = new QueryPersistanceInterface();
		qt.field_name = "CUSTOMER";
		qt.seqno = "0";
		qt.field_type = "CF";
		result.put("Row", qt);
		qt = new QueryPersistanceInterface();
		qt.field_name = "CITY";
		qt.seqno = "1";
		qt.field_type = "CF";
		result.put("Row", qt);
		qt = new QueryPersistanceInterface();
		qt.field_name = "CUSTOMER_TYPE";
		qt.seqno = "2";
		qt.field_type = "CF";
		result.put("Row", qt);
		return result;
	}

}
