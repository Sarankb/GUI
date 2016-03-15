package com.ka.gui.components;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.gwt.user.client.ui.Widget;
import com.ka.gui.persistance.QueryPersistance;
import com.ka.gui.persistance.interfaces.QueryPersistanceInterface;
import com.vaadin.addon.contextmenu.GridContextMenu.GridContextMenuOpenListener;
import com.vaadin.addon.contextmenu.Menu.Command;
import com.vaadin.client.widget.grid.CellReference;
import com.vaadin.client.widget.grid.CellStyleGenerator;
import com.vaadin.addon.contextmenu.MenuItem;
import com.vaadin.addon.contextmenu.ContextMenu;
import com.vaadin.addon.contextmenu.GridContextMenu;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.event.SortEvent;
import com.vaadin.event.SortEvent.SortListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.grid.GridConstants.Section;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")

public class GridQuery extends UI {
	
	AbstractLayout vlincoming = new VerticalLayout();
	VerticalLayout vlmain = new VerticalLayout();
	VerticalLayout vlpopup = new VerticalLayout();
	ArrayList<HeaderRow> hrows;
	final Grid gridmain = new Grid();
	Container gridct = gridmain.getContainerDataSource();
	com.google.common.collect.Table<String, String, String> orig_data = HashBasedTable.create();
	QueryPersistanceInterface qinf = new QueryPersistanceInterface();
	QueryPersistance qp = new QueryPersistance();
	SetMultimap<String, QueryPersistanceInterface> query_data;
	SetMultimap<String, QueryPersistanceInterface> query_data_temp;
	Map<String,Integer> col_list;
	ArrayList<String> col_values = new ArrayList<String>();
	final GridContextMenu context = new GridContextMenu(gridmain);  
	String mnu_clicked_row = null;
	String mnu_clicked_column = null;
	Integer mnu_clicked_column_index = -1;
	final String vf_field = "Value Fields";
	String last_row = null;	
	Integer no_cols = -1;
	GridContextMenuOpenListener gridlistener; 
	String sort_default = "Asc";
	String sort_column = "Dsc";
	ArrayList<String> sort_asc = new ArrayList<String>();
	ArrayList<String> sort_dsc = new ArrayList<String>();	
	ArrayList<String> sort_order = new ArrayList<String>();
	List<SortOrder> sort_list;
	Set<String> tab_row;
	Set<String> tab_col;
	Integer vf_seqno;
	Multimap<Integer, String> breakup_row;
	Multimap<Integer, String> breakup_column;
	
// Commands used in Menus
	

     Command cmd_sort = new Command() {
		
		@Override
		public void menuSelected(MenuItem selectedItem) {
			// TODO Auto-generated method stub
			
			if (mnu_clicked_row != null)
			{
				if (selectedItem.getText().equals("Ascending"))
				{
					if(selectedItem.isChecked())
					{
						gridmain.sort(mnu_clicked_row, SortDirection.ASCENDING);
						if (sort_dsc.contains(mnu_clicked_row))
						sort_dsc.remove(mnu_clicked_row);
						sort_asc.add(mnu_clicked_row);
					}
					else
					{
// if unchecked then the default sort order is descending 						
						gridmain.sort(mnu_clicked_row, SortDirection.DESCENDING);
						if (sort_asc.contains(mnu_clicked_row))
						sort_asc.remove(mnu_clicked_row);
						sort_dsc.add(mnu_clicked_row);
					}
					
				}
				else
				{
					if(selectedItem.isChecked())
					{ 
						gridmain.sort(mnu_clicked_row, SortDirection.DESCENDING);
						if (sort_asc.contains(mnu_clicked_row))
						sort_asc.remove(mnu_clicked_row);
						sort_dsc.add(mnu_clicked_row);
					}
					else
					{
// if unchecked then the default sort order is ascending 
						gridmain.sort(mnu_clicked_row, SortDirection.ASCENDING);
						if (sort_dsc.contains(mnu_clicked_row))
						sort_dsc.remove(mnu_clicked_row);
						sort_asc.add(mnu_clicked_row);
					}
				}
			}
			else if(mnu_clicked_column != null)
			{
				if (selectedItem.getText().equals("Ascending"))
				{
					if(selectedItem.isChecked())
					{
						gridmain.sort(mnu_clicked_column, SortDirection.ASCENDING);
						if (sort_dsc.contains(mnu_clicked_column))
						sort_dsc.remove(mnu_clicked_column);
						sort_asc.add(mnu_clicked_column);
					}
					else
					{
						gridmain.sort(mnu_clicked_column, SortDirection.DESCENDING);
						if (sort_asc.contains(mnu_clicked_column))
						sort_asc.remove(mnu_clicked_column);
						sort_dsc.add(mnu_clicked_column);
					}
					
				}
				else
				{
					if(selectedItem.isChecked())
					{

						gridmain.sort(mnu_clicked_column, SortDirection.DESCENDING);
						if (sort_asc.contains(mnu_clicked_column))
						sort_asc.remove(mnu_clicked_column);
						sort_dsc.add(mnu_clicked_column);	
					}
					else
					{
						gridmain.sort(mnu_clicked_column, SortDirection.ASCENDING);
						if (sort_dsc.contains(mnu_clicked_column))
						sort_dsc.remove(mnu_clicked_column);
						sort_asc.add(mnu_clicked_column);
					}
				}
			}
			else if(!mnu_clicked_column_index.equals(-1))
			{
				String field_name = gridmain.getHeaderRow(mnu_clicked_column_index).getCell(last_row).getText();
				if (selectedItem.getText().equals("Ascending"))
				{
					if(selectedItem.isChecked())
						sort_column = "Asc";
					else
					sort_column = "Dsc";
				}
				else
				{
					if(selectedItem.isChecked())
						sort_column = "Dsc";
					else
						sort_column = "Asc";
				}				
				gridct.removeAllItems();				
				remove_columns();
				populate_table();
				if(sort_column.equals("Asc"))
				{
					if (sort_dsc.contains(field_name))
						sort_dsc.remove(field_name);
					sort_asc.add(field_name);
				}
				else
				{
					if (sort_asc.contains(field_name))
						sort_asc.remove(field_name);
					sort_dsc.add(field_name);
				}
			  }
			
			else
			{
				Notification.show("You have not selected any field");
			}
		}
	};
	
    Command cmd_sort_order = new Command() {
		
		@Override
		public void menuSelected(MenuItem selectedItem) {
			// TODO Auto-generated method stub
		   final Window win = new Window();
		   VerticalLayout vl = new VerticalLayout();
		   HorizontalLayout hl_btn = new HorizontalLayout();
		   HorizontalLayout hl_list = new HorizontalLayout();
		   Panel pl  = new Panel("Fields",hl_list);
		   Button btn_ok = new Button("Apply");
		   btn_ok.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				sort_list = gridmain.getSortOrder();
				//sort_list.clear();
				win.close();
				getUI().getCurrent().removeWindow(win);
			}
		});
		   Button btn_cancel = new Button("Cancel");
		   hl_btn.addComponent(btn_ok);
		   hl_btn.addComponent(btn_cancel);
		   vl.addComponent(pl);
		   vl.addComponent(hl_btn);
		//Add new table with options for adding and removing items
		   final Table tb = new Table();		   
		   tb.setColumnHeaderMode(ColumnHeaderMode.ID);
		   tb.setHeight(20, Unit.EM);
		   tb.addContainerProperty("FieldName", ComboBox.class, null);
		   tb.addContainerProperty("SortType", ComboBox.class, null);
		   tb.addContainerProperty("Remove", Button.class, null);
		   hl_list.addComponent(tb);	
		   
		   final ClickListener btn_remove_listener = new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Button bt = event.getButton();
				if(!(StringUtils.isEmpty(tb.getContainerProperty(bt.getData(),"FieldName").getValue().toString())))
				{
					sort_order.remove(tb.getContainerProperty(bt.getData(),"FieldName").getValue());
				}
				tb.removeItem(bt.getData());				
			}
		};
			Button btn_add = new Button("Add");		   
			btn_add.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				   tb.setImmediate(true);
				   Object s = tb.addItem();
				   ComboBox cmb_field = new ComboBox();				   
				   fill_combofield(cmb_field);
				   ComboBox cmb_sort = new ComboBox();
				   fill_combosort(cmb_sort);
				   Button btn_remove = new Button();
				   btn_remove.setData(s);
				   btn_remove.addClickListener(btn_remove_listener);
				   tb.getContainerProperty(s, "FieldName").setValue(cmb_field);
				   tb.getContainerProperty(s, "SortType").setValue(cmb_sort);
				   tb.getContainerProperty(s, "Remove").setValue(btn_remove);
			}
		});
		    hl_list.addComponent(btn_add);
		    sort_order.clear();
		    for(String s:sort_asc)
		    {
			   Object is = tb.addItem();
			   ComboBox cmb_field = new ComboBox();
			   fill_combofield(cmb_field);
			   ComboBox cmb_sort = new ComboBox();
			   fill_combosort(cmb_sort);
			   Button btn_remove = new Button();
			   btn_remove.setData(is);
			   btn_remove.addClickListener(btn_remove_listener);
			   tb.getContainerProperty(is, "FieldName").setValue(cmb_field);
			   tb.getContainerProperty(is, "SortType").setValue(cmb_sort);
			   tb.getContainerProperty(is, "Remove").setValue(btn_remove);
			   cmb_field.setValue(s);
			   cmb_field.setEnabled(false);
			   cmb_sort.setValue(SortDirection.ASCENDING);
			   sort_order.add(s);
		   }
		   for(String s:sort_dsc)
		   {
			   Object is = tb.addItem();
			   ComboBox cmb_field = new ComboBox();
			   fill_combofield(cmb_field);
			   ComboBox cmb_sort = new ComboBox();
			   fill_combosort(cmb_sort);
			   Button btn_remove = new Button();
			   btn_remove.setData(is);
			   btn_remove.addClickListener(btn_remove_listener);
			   tb.getContainerProperty(is, "FieldName").setValue(cmb_field);
			   tb.getContainerProperty(is, "SortType").setValue(cmb_sort);
			   tb.getContainerProperty(is, "Remove").setValue(btn_remove);
			   cmb_field.setValue(s);
			   cmb_field.setEnabled(false);
			   cmb_sort.setValue(SortDirection.DESCENDING);
			   sort_order.add(s);
		   }
		   win.setCaption("Sort Order");
		   win.setContent(vl);
		   win.setModal(true);
		   getUI().getCurrent().addWindow(win);
		}
	};

	
  
	
// Start of Constructor and other methods
	
	public GridQuery(AbstractLayout vl,QueryPersistance qip) {
		// TODO Auto-generated constructor stub
	    vlincoming = vl;
	   // qp = qip;
	   // demo to be removed later
	    qp.query_Id = "4B4136303630373233";
		draw_table();
		
	}
	
	private void draw_menulayouts()
	{
		Panel pn = new Panel();
		pn.setCaption("Filter");
		vlpopup.addComponent(pn);
	}
	private void set_table_prop()
	{
	    query_data = qp.get_Query();
	    query_data.replaceValues("Row", fix_query_vf(query_data.get("Row")));
	    query_data.replaceValues("Column", fix_query_vf(query_data.get("Column")));	    
	    tab_row = orig_data.rowKeySet();
	    tab_col = orig_data.columnKeySet();
	    split_query(tab_row, tab_col);
	    		
		SortedMap<Integer, String> col_list = new TreeMap<Integer,String>();		
		Iterator<QueryPersistanceInterface> itr = query_data.get("Row").iterator();	
		while (itr.hasNext())
		{
			QueryPersistanceInterface qtemp = itr.next();		
			if ( qtemp.field_type.equals("VF") )
			{
				if(gridmain.getColumn(vf_field) == null)
				{
					gridmain.addColumn(vf_field, String.class);		    
					gridmain.getColumn(vf_field).setHeaderCaption(vf_field);					
					col_list.put(Integer.parseInt(qtemp.seqno), vf_field);
				}
			}
			else
			{
				gridmain.addColumn(qtemp.field_name, String.class);		    
				gridmain.getColumn(qtemp.field_name).setHeaderCaption(qtemp.field_name);
				col_list.put(Integer.parseInt(qtemp.seqno), qtemp.field_name);
			}
		    
		}		
		last_row = col_list.get(col_list.lastKey());			
		no_cols = size(query_data.get("Column"));
		hrows = new ArrayList<Grid.HeaderRow>(no_cols);
		for(int i=0;i<no_cols;i++)
		{
			HeaderRow h1 =  gridmain.addHeaderRowAt(0);			
			hrows.add(0, h1);		
		   // gridmain.addColumn(qtemp.field_name, String.class);
		   // gridmain.getColumn(qtemp.field_name).setHeaderCaption(qtemp.field_name);		
		}
		for(int i=0;i<no_cols;i++)
		{
		itr = query_data.get("Column").iterator();
		while (itr.hasNext())
		{
			QueryPersistanceInterface qtemp = itr.next();
			if (qtemp.seqno.equals(Integer.toString(i)))
			{
			  if (qtemp.field_type.equals("VF"))
			  {
				  hrows.get(i).getCell(last_row).setText(vf_field);
			  }
			  else
			  {	  
				 hrows.get(i).getCell(last_row).setText(qtemp.field_name);		
			  }
			}
		 }
		}
	  gridmain.setColumnOrder(col_list.values().toArray());	   
	  
      
	}
	private boolean field_exists(String row_column,String field_name)
	{
		boolean result = false;
		Iterator<QueryPersistanceInterface> qip = query_data.get(row_column).iterator();
		while(qip.hasNext())
		{
			QueryPersistanceInterface qint = qip.next();
			if (qint.field_name.equals(field_name) && !qint.field_type.equals("VF"))
			{
				result = true;
				return result;
			}
			else if (field_name.equals(vf_field) && qint.field_type.equals("VF"))
			{
				result = true;
				return result;
			}
		}
		
		return result;
	}
	private void set_context_menu()
	{
		gridlistener = new GridContextMenuOpenListener() {
			
			@Override
			public void onContextMenuOpen(GridContextMenuOpenEvent event) {
				// TODO Auto-generated method stub
				
				if (event.getSection() == Section.BODY)
				{
					 // Check if the selected item is in row or column.
				  String heading = gridmain.getHeaderRow(size(query_data.get("Column"))).getCell(event.getPropertyId()).getText();
				  if(field_exists("Row", heading))
					{
					//	Notification.show("Row Clicked");	
						mnu_clicked_row = event.getPropertyId().toString();
						mnu_clicked_column = null;
						mnu_clicked_column_index = -1;						
						add_menu(context,"Row",event.getSection(),mnu_clicked_row);
						// Show the menus for Row, Sorting, CF( to check if VF or CF Later )
					}
					else
					{
					    // Notification.show("Column Clicked");
						// Show menus for Body( Numbers only ) like Format only
						mnu_clicked_column = event.getPropertyId().toString();
						mnu_clicked_row = null;
						mnu_clicked_column_index = -1;
						add_menu(context,"Column",event.getSection(),mnu_clicked_column);
					}
					
				}
				else if(event.getSection() == Section.HEADER)
				{
					String heading = gridmain.getHeaderRow(size(query_data.get("Column"))).getCell(event.getPropertyId()).getText();
					String clicked_text = gridmain.getHeaderRow(event.getRowIndex()).getCell(event.getPropertyId()).getText();
					if(field_exists("Row", heading))
					{
												
						if (StringUtils.isEmpty(clicked_text) || heading.equals(clicked_text))
						{
// if clicked item is in the header row							
							mnu_clicked_column = null;
							mnu_clicked_column_index = -1;
							mnu_clicked_row = event.getPropertyId().toString();
							add_menu(context,"Row",event.getSection(),mnu_clicked_row);
						}
						else
						{
// if clicked item is in the header column							
							mnu_clicked_column = null;
							mnu_clicked_row = null;
							mnu_clicked_column_index = event.getRowIndex();
							add_menu(context,"Column",event.getSection(),gridmain.getHeaderRow(event.getRowIndex()).getCell(event.getPropertyId()).getText());
						}
						
						// Show the menus for Row,filter, CF( to check if VF or CF Later )
					}
					else
					{
							mnu_clicked_column = event.getPropertyId().toString();
							mnu_clicked_row = null;
							mnu_clicked_column_index = -1;
							add_menu(context,"Column",event.getSection(),mnu_clicked_column);
						// Show menus for Body( Numbers only ) like Format only
					}
				}
				else if(event.getSection() == Section.FOOTER)
				{
					
				}
			}
		};
		
		context.setAsContextMenuOf(gridmain);
		context.addGridHeaderContextMenuListener(gridlistener);
		context.addGridBodyContextMenuListener(gridlistener);
		context.addGridFooterContextMenuListener(gridlistener);
		
	}
	private int size(Set<QueryPersistanceInterface> q)
	{
		int result = 0;
		boolean has_vf = false;
		for(QueryPersistanceInterface a:q)
		{
			if(a.field_type.equals("VF"))
			{
				has_vf = true;
			}
			else
			{
				result++;
			}
		}
		if (has_vf)
		   result++;
		return result;
	}
	private boolean validate_structure()
	{
	   boolean result = false;
	   Set<String> tab_row =  orig_data.rowKeySet();
	   Set<String> tab_col = orig_data.columnKeySet();
	   if (tab_row != null)
	   {
	      Iterator<String> itr = tab_row.iterator();
		  if(itr.hasNext())
		  {
	        String[] rows = StringUtils.split(itr.next(),"|");
	        if (size(query_data.get("Row")) == rows.length)
	        {
	        	result = true;
	        }
	        else
	        {
	        	result = false;
	        	return result;
	        }
		  }    
	    }
	   if (tab_col != null)
	   {
	      Iterator<String> itr = tab_col.iterator();
		  if(itr.hasNext())
		  {
	        String[] cols = StringUtils.split(itr.next(),"|");
	        if (size(query_data.get("Column")) == cols.length)
	        {
	        	result = true;
	        }
	        else
	        {
	        	result = false;
	        }
		  }    
	    }
	   return result;
	}
	private Set<String> sort_set(Set<String> source,String Ordering)
	{
		Set<String> result = null;
		Comparator<String> comp1 = new Comparator<String>() {
			
			@Override
			public int compare(String o1, String o2) {
				// TODO Auto-generated method stub
				return o2.compareTo(o1);
			}
		};
		
		if(Ordering.equals("Asc"))
		{
			result = new TreeSet<String>(source);
		}
		else if(Ordering.equals("Dsc"))
		{
			result = new TreeSet<String>(comp1);
		    result.addAll(source);
		}
		return result;
	}
	private Set<QueryPersistanceInterface> fix_query_vf(Set<QueryPersistanceInterface> q)
	{
		Set<QueryPersistanceInterface> result = new HashSet<QueryPersistanceInterface>();
		vf_seqno = -1;		
		ArrayList<Integer> vf_all = new ArrayList<Integer>();
		for(QueryPersistanceInterface a:q)
		{
			if (a.field_type.equals("VF"))
			{
				vf_all.add(Integer.parseInt(a.seqno));
				if(vf_seqno > Integer.parseInt(a.seqno) || vf_seqno == -1)
				{
					vf_seqno = Integer.parseInt(a.seqno);
				}
				
			}
		}
		if(vf_all.isEmpty())
		{
			result.addAll(q);
		}
		else
		{
			Integer curr_seqno = vf_seqno;
			for(QueryPersistanceInterface a:q)
			{
				if (a.field_type.equals("VF"))
				{
				   QueryPersistanceInterface b =  new QueryPersistanceInterface();
				   b.field_name = a.field_name;
				   b.field_type = a.field_type;
				   b.seqno = vf_seqno.toString();
				   result.add(b);
				}
				else
				{
					if (Integer.parseInt(a.seqno) < curr_seqno)
					{
					result.add(a);
					}
					else
					{
						curr_seqno++;
						QueryPersistanceInterface b =  new QueryPersistanceInterface();
						b.field_name = a.field_name;
						b.field_type = a.field_type;
						b.seqno = curr_seqno.toString();
						result.add(b);
					}
				}
				
			}
		}
		return result;
		
	}
	private void populate_table()
	{
		tab_col = 	sort_set(tab_col, sort_default);
		tab_row =   sort_set(tab_row,sort_default);
		
		gridmain.setImmediate(true);
		if (!mnu_clicked_column_index.equals(-1))
		{
			tab_col = sort_column(tab_col, mnu_clicked_column_index, sort_column);
		}
		
		for(String r:tab_row)
		{
		   
		   Object itemid = gridct.addItem();
		   String[] line_row = StringUtils.split(r, "|");
		   Iterator<QueryPersistanceInterface> itr_row = query_data.get("Row").iterator();
		   
		   while(itr_row.hasNext())
		   {
			   QueryPersistanceInterface qtemp = itr_row.next();			
			   if (qtemp.field_type.equals("VF"))
			   {
				   gridct.getContainerProperty(itemid,vf_field).setValue(line_row[Integer.parseInt(qtemp.seqno)]);
			   }
			   else
			   {
				   gridct.getContainerProperty(itemid,qtemp.field_name).setValue(line_row[Integer.parseInt(qtemp.seqno)]);
			   }
		   }	 
			
			for(String c:tab_col)
		   {	
				String[] line_col = StringUtils.split(c, "|");  
				
				if (gridmain.getColumn(c)==null)
				  {
					  gridmain.addColumn(c);					  
				  }
				  gridct.getContainerProperty(itemid,c).setValue(orig_data.get(r, c));
				  gridmain.getColumn(c).setHeaderCaption(" ");
				  gridmain.getColumn(c).setWidthUndefined();
				  for(int i=0;i<hrows.size();i++)
				  {
					  if(hrows.get(i).getCell(last_row).getText().equals(vf_field))
					  {
						  hrows.get(i).getCell(c).setText(line_col[vf_seqno]);
					  }
					  else
					  {
						  hrows.get(i).getCell(c).setText(line_col[i]);	
					  }
				  }
				
		   }		
		}
		repeated_headers();
	}
	private void split_query(Set<String> row,Set<String> column)
	{
		breakup_row = LinkedHashMultimap.create();
		breakup_column = LinkedHashMultimap.create();
		String[] line = null;
// Split Rows
		sort_set(row, sort_default);
		for(String c:row)
		{

			line = StringUtils.split(c, "|");
			for(int i = 0;i< line.length;i++)
			{
				breakup_row.put(i, line[i]);
			}
		}
//Split Columns 
		sort_set(column,sort_default);		
		for(String c:column)
		{

			line = StringUtils.split(c, "|");
			for(int i = 0;i< line.length;i++)
			{
				breakup_column.put(i, line[i]);
			}
		}
	}
	private Set<String> sort_column(Set<String> input,Integer col_index,String sort_type)
	{
		Set<String> result = new LinkedHashSet<String>();
		Set<String> res = new LinkedHashSet<String>();
		String[] line_col = null;
		Multimap<Integer, String> breakup = HashMultimap.create();
		for(String c:input)
		{
// number of columns			
			line_col = StringUtils.split(c, "|");
			for(int i = 0;i< line_col.length;i++)
			{
				breakup.put(i, line_col[i]);
			}
		}
		if(line_col != null)
		{
		    Set<Integer> tempi = breakup.keySet();
		    Multimap<Integer, String> breakup_temp = LinkedHashMultimap.create();
			for(Integer i:tempi)
		    {
			  if(i.equals(col_index))
			  {
				 Set<String> temp =  sort_set((Set<String>) breakup.get(i),sort_type);
				 breakup_temp.putAll(i,temp);
			  }
			  else
			  {
				  Set<String> temp =  sort_set((Set<String>) breakup.get(i),"Asc");
				  breakup_temp.putAll(i,temp);
			  }
		  }
		  
		  Set<String> first_level = (Set<String>) breakup_temp.get(0);
		  for(String s:first_level)
		  {
			 
			 if (breakup_temp.containsKey(1))
			 {
				 res.addAll(concatenate_string(1, breakup_temp,s));
			 }
			 else
			 {
				 res.add(s);
			 }
			 
		  }
		}
		for(String s:res)
		{
		   if (input.contains(s))
		   {
			  result.add(s);
		   }
		}
		return result;
	}
	private Set<String> concatenate_string(Integer level,Multimap<Integer,String> orig_data,String passthrough)
	{
		Set<String> res = (Set<String>) orig_data.get(level);
		Set<String> result = new LinkedHashSet<String>();
		for(String s:res )
		{
			if(orig_data.containsKey(level + 1))
			{
				result.addAll(concatenate_string(level + 1, orig_data,passthrough+"|"+s));
				
			}
			else
			{
				result.add(passthrough+"|"+ s);
			}
			
		}
		return result;
	}
	private void set_grid_properties()
	{
		gridmain.removeAllColumns();	
		gridct.removeAllItems();
		gridmain.setColumnReorderingAllowed(false);
		gridmain.setEditorEnabled(false);
		gridmain.setSelectionMode(SelectionMode.NONE);
		gridmain.clearSortOrder();		
		gridmain.setHeightByRows(10);		
		gridmain.setFooterVisible(true);
		gridmain.setWidth(100, Unit.PERCENTAGE);
		gridmain.addSortListener(new SortListener() {
			
			@Override
			public void sort(SortEvent event) {
				// TODO Auto-generated method stub
				sort_list = event.getSortOrder();	
				for(SortOrder s:sort_list)
				{
					if (s.getDirection() == SortDirection.ASCENDING)
					{
						if( sort_dsc.contains(s.getPropertyId()))
							sort_dsc.remove(s.getPropertyId());
						  sort_asc.add(s.getPropertyId().toString());
					}  
					else
					{
						if( sort_asc.contains(s.getPropertyId()))
							sort_asc.remove(s.getPropertyId());
						    sort_dsc.add(s.getPropertyId().toString());
						
					}
				}
			}
		});
	}
	private void fill_demo_Data()
	{
		orig_data.clear();
		
//		orig_data.put("M1|Amount", "C1|CT1|T1","100");
//		orig_data.put("M1|Amount", "C2|CT1|T1","200");
//		orig_data.put("M1|Amount", "C2|CT2|T1","300");
//		orig_data.put("M2|Amount", "C1|CT1|T1","150");
//		orig_data.put("M2|Amount", "C2|CT1|T1","250");
//		orig_data.put("M2|Amount", "C2|CT2|T1","350");
//		orig_data.put("M1|Amount", "C1|CT1|T2","1000");
//		orig_data.put("M1|Amount", "C2|CT1|T2","2000");
//		orig_data.put("M1|Amount", "C2|CT2|T2","3000");
//		orig_data.put("M2|Amount", "C1|CT1|T3","1500");
//		orig_data.put("M2|Amount", "C2|CT1|T4","2500");
//		orig_data.put("M2|Amount", "C2|CT2|T2","3500");
//		orig_data.put("M1|Quant", "C1|CT1|T1","1");
//		orig_data.put("M1|Quant", "C3|CT1|T3","2");
//		orig_data.put("M1|Quant", "C2|CT2|T1","3");
//		orig_data.put("M2|Quant", "C1|CT1|T3","1");
//		orig_data.put("M2|Quant", "C2|CT1|T2","2");
//		orig_data.put("M2|Quant", "C3|CT2|T4","3");
		
		orig_data.put("C1|CT1|T1","M1|Amount|O1","100");
		orig_data.put("C2|CT1|T1","M1|Amount|O2","200");
		orig_data.put("C2|CT2|T1","M1|Amount|O5", "300");
		orig_data.put("C1|CT1|T1","M2|Amount|O1", "150");
		orig_data.put("C2|CT1|T1","M2|Amount|O3", "250");
		orig_data.put("C2|CT2|T1","M2|Amount|O2", "350");
		orig_data.put("C1|CT1|T2","M1|Amount|O1", "1000");
		orig_data.put("C2|CT1|T2","M1|Amount|O2", "2000");
		orig_data.put("C2|CT2|T2","M1|Amount|O3", "3000");
		orig_data.put("C1|CT1|T3","M2|Amount|O1", "1500");
		orig_data.put("C2|CT1|T4","M2|Amount|O3", "2500");
		orig_data.put("C2|CT2|T2","M2|Amount|O5", "3500");
		orig_data.put("C1|CT1|T1","M1|Quant|O1", "1");
		orig_data.put("C3|CT1|T3","M1|Quant|O3", "2");
		orig_data.put("C2|CT2|T1","M1|Quant|O2", "3");
		orig_data.put("C1|CT1|T3","M2|Quant|O1", "1");
		orig_data.put("C2|CT1|T2","M2|Quant|O5", "2");
		orig_data.put("C3|CT2|T4","M2|Quant|O1", "3");
		
		
	}
	private void remove_columns()
	{
		for (Column o : gridmain.getColumns()) {
			
		 if(! field_exists("Row",(String) o.getPropertyId()))
			{
			   gridmain.removeColumn(o.getPropertyId()); 
			}
		}
	}
	private void add_menu(ContextMenu cmenu,String row_column,Section section,String fieldname)
	{
		cmenu.removeItems();
		com.vaadin.addon.contextmenu.MenuItem item =  cmenu.addItem("Query", null);
		MenuItem subitem;
		item.addItem("Open", null);
		item.addItem("New", null);
		if (row_column.equals("Row") || (row_column.equals("Column") && section == Section.HEADER))
		{
			item = cmenu.addItem("Filter", null);
			item.addItem("CF",null);
			item.addItem("New", null);
			cmenu.addSeparator();
		}
		if( row_column.equals("Row"))
		{
			item = cmenu.addItem("Row", null);
			item.addItem("Add CF", null);
			item.addItem("Change Order", null);		
		}
		else if(row_column.equals("Column"))
		{
			item = cmenu.addItem("Column", null);
			item.addItem("Add CF", null);
			item.addItem("Change Order", null);
		}
		cmenu.addSeparator();
		item = cmenu.addItem("Sort", null);
		if (mnu_clicked_column_index.equals(-1))
// if clicked on a area other than the name of the Field in Column		
		{
			subitem =  item.addItem("Ascending", cmd_sort);
			subitem.setCheckable(true);
			if (!(sort_asc.contains(fieldname) || sort_dsc.contains(fieldname)))
			{
// Default sorting is Ascending			
			 subitem.setChecked(true);
			 sort_asc.add(fieldname);
			}
			else if(sort_asc.contains(fieldname))
			{
			 subitem.setChecked(true);
			}
			subitem = item.addItem("Descending", cmd_sort);
		    subitem.setCheckable(true);
		    if( sort_dsc.contains(fieldname))
		    {
		    	subitem.setChecked(true);
		    }
		}
		else
// if user has clicked on the name of the field in the column then checking is done against that name and not the field name		
		{
			String field_name = gridmain.getHeaderRow(mnu_clicked_column_index).getCell(last_row).getText();
			subitem =  item.addItem("Ascending", cmd_sort);
			subitem.setCheckable(true);
			if (!(sort_asc.contains(field_name) || sort_dsc.contains(field_name)))
			{
// Default sorting is Ascending			
			 subitem.setChecked(true);
			 sort_asc.add(field_name);
			}
			else if(sort_asc.contains(field_name))
			{
			 subitem.setChecked(true);
			}
			subitem = item.addItem("Descending", cmd_sort);
		    subitem.setCheckable(true);
		    if( sort_dsc.contains(field_name))
		    {
		    	subitem.setChecked(true);
		    }
		}
		cmenu.addSeparator();
		cmenu.addItem("Sort Order", cmd_sort_order);
		cmenu.addSeparator();
		if (row_column.equals("Row") || (row_column.equals("Column") && section == Section.HEADER))
		{
// dont show display option for Value fields			
			if(! (StringUtils.contains(fieldname, "|") || fieldname.equals(vf_field) ) )
			{	
			
				item = cmenu.addItem("Display", null);
				subitem =  item.addItem("ID", null);
				subitem.setCheckable(true);
				subitem.setChecked(true);
				item.addItem("Description", null);
				item.addItem("ID + Description", null);
				item.addItem("Description + ID", null);
				item = cmenu.addItem("Text", null);
				item.addItem("Short", null);
				item.addItem("Medium", null);
				item.addItem("Long",null);
			}
		}
	}
	private void fill_combofield(ComboBox c1)
	{
		c1.clear();
		for(QueryPersistanceInterface qint:query_data.get("Row"))
		{
			if(!qint.field_type.equals("VF"))
			{
				
				c1.addItem(qint.field_name);
			}
			
		}
		for(QueryPersistanceInterface qint:query_data.get("Column"))
		{
			if(!qint.field_type.equals("VF"))			
				c1.addItem(qint.field_name);
			
		}
	}
	private void fill_combosort(ComboBox c1)
	{
		c1.clear();
		c1.addItem(SortDirection.ASCENDING);
		c1.addItem(SortDirection.DESCENDING);
	}
	private void repeated_headers()
	{
// merge cells along column in different rows( ROW STRUCTURE)		
		for(Object a:gridct.getContainerPropertyIds())
		{
			if(StringUtils.contains(a.toString(),"|"))
			{
				
			}
			else
			{
				String value = "";
				for(Object i:gridct.getItemIds())
				{
					if(StringUtils.isEmpty(value))
					{
						value = gridct.getContainerProperty(i, a).getValue().toString();
					}
					else
					{
						if(gridct.getContainerProperty(i, a).getValue().equals(value))
						{
							gridct.getContainerProperty(i, a).setValue("");
						}
						else
						{
							value = gridct.getContainerProperty(i, a).getValue().toString();
						}
					}
				}
			}
		}
//merge cells along column in different rows( COLUMN STRUCTURE )
		System.out.println(gridct.getContainerPropertyIds());
		
		for(HeaderRow h:hrows)
		{
			String value = "";
			for(Object a:gridct.getContainerPropertyIds())
			{
				if(StringUtils.isEmpty(value))
				{
					value = h.getCell(a).getText();
				}
				else
				{
					if(h.getCell(a).getText().equals(value))
					{
						h.getCell(a).setText("");					
						
					}
					else
					{
						value = h.getCell(a).getText();
						
					}
				}
			
			}
		}
		
	}
	private void draw_table()
	{
		if (orig_data.isEmpty())		
			fill_demo_Data();
		else
			fill_demo_Data();
		set_grid_properties();
		set_table_prop();
		set_context_menu();
		vlincoming.setImmediate(true);			
        vlmain.setImmediate(true);        
       // vlmain.addComponent(gridmain);
        vlincoming.addComponent(gridmain);
        vlincoming.setSizeFull();
        vlmain.setSizeFull();       
        draw_menulayouts();
        if (validate_structure())
        {
        	populate_table();           
        }           
        else
        {
        	Notification.show("Inconsistent Query Structure with Data");
        }
	}
			
	@Override
	protected void init(VaadinRequest request) {
		// TODO Auto-generated method stub
	}

}
