package cl.ps.asistencia;

import javax.servlet.annotation.WebServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI 
{
	
	Logger logger = LoggerFactory.getLogger(MyVaadinUI.class);

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "cl.ps.asistencia.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }
    
    Navigator navigator;

    @Override
    protected void init(VaadinRequest request) {
    	
    	//agregando navigator
    	navigator = new Navigator(this,this);
    	
    	// Create and register the views
        navigator.addView(FichaHome.FICHAVIEW, new FichaHome());
        navigator.addView(CSV.CSV, new CSV());
    	if(navigator.getState() == null || navigator.getState().isEmpty())
    		navigator.navigateTo(CSV.CSV);
      }

}
