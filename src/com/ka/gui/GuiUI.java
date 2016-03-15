package com.ka.gui;

import javax.servlet.annotation.WebServlet;

import com.google.common.collect.Table;
import com.ka.gui.components.GridQuery;
import com.vaadin.addon.contextmenu.GridContextMenu;
import com.vaadin.addon.contextmenu.GridContextMenu.GridContextMenuOpenListener;
import com.vaadin.addon.contextmenu.GridContextMenu.GridContextMenuOpenListener.GridContextMenuOpenEvent;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("gui")
public class GuiUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(ui = GuiUI.class, productionMode = false, widgetset = "com.ka.gui.widgetset.GuiWidgetset")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		setContent(layout);
     //   TableQuery tq = new TableQuery(layout, null); 		
       new GridQuery(layout, null);
        
	}

}