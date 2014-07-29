package cl.ps.asistencia.ui;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.ps.asistencia.MyVaadinUI;
import cl.ps.asistencia.util.Constantes;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

//@Component
//@Scope("prototype")
//@VaadinView(value = Login.NAME, cached = true)

public class Login extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -6594032271391490796L;

	Logger logger = LoggerFactory.getLogger(Login.class);
	
//	@Autowired
//	private transient AuthenticationManager authenticationManager;
//	@Autowired
//	private transient UserService servicioUsuario;
	
	List<OnAuthentificated> listeners = new LinkedList<OnAuthentificated>();
	public void addListener(OnAuthentificated event){
		listeners.add(event);
	}
	
	private void doEvent(){
		for (OnAuthentificated listener : listeners) {
			listener.authentificated();
		}
	}
	
	public interface OnAuthentificated{
		public void authentificated();
	}
	
	public static final String NAME = "";
    
    public Login() {
    	
    	setSizeFull();
    	addStyleName("login-layout");
    	
		final CssLayout loginPanel = new CssLayout();
        loginPanel.addStyleName("login-panel");

        HorizontalLayout labels = new HorizontalLayout();
        labels.setWidth("100%");
        labels.setMargin(true);
        labels.addStyleName("labels");
        loginPanel.addComponent(labels);

        Label welcome = new Label("Bienvenido");
        welcome.setSizeUndefined();
        welcome.addStyleName("h4");
        labels.addComponent(welcome);
        labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);

        Label title = new Label("Sistema Producción");
        title.setSizeUndefined();
        title.addStyleName("h2");
        title.addStyleName("light");
        labels.addComponent(title);
        labels.setComponentAlignment(title, Alignment.MIDDLE_RIGHT);

        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.setMargin(true);
        fields.addStyleName("fields");

        final TextField username = new TextField("Usuario");
        username.focus();
        fields.addComponent(username);

        final PasswordField password = new PasswordField("Password");
        fields.addComponent(password);

        final Button signin = new Button("Entrar");
        signin.addStyleName("default");
        fields.addComponent(signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        final ShortcutListener enter = new ShortcutListener("Entrar",
                KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                signin.click();
            }
        
        };
		
        signin.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				String u = username.getValue();
				String p = password.getValue();
				
				 if (u.isEmpty() || p.isEmpty()) {
					 Notification.show("Deben ser ingresados el usuario y el password ",Type.ERROR_MESSAGE);
		            } 
				 else {
					 try {
//		                    UsernamePasswordAuthenticationToken token = 
//		                            new UsernamePasswordAuthenticationToken(u, p);
//		                    
//		                    Authentication authentication = authenticationManager.authenticate(token);
//
//		                    // Set the authentication info to context 
//		                    SecurityContext securityContext = SecurityContextHolder.getContext();
//		                    securityContext.setAuthentication(authentication);
//		                    VaadinSession.getCurrent().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
//		                    //busca el usuario en base de datos para guardarlo en la sessión
						 
						 	if("admin".equals(u) && "admin".equals(p )){
			                    Boolean usuarioLogeado = Boolean.TRUE;
			                    VaadinSession.getCurrent().setAttribute(Constantes.SESSION_USUARIO, usuarioLogeado);
	//		                    logger.debug("Login authentication "+authentication);
	//		                    signin.removeShortcutListener(enter);
	//		                    
			                    doEvent();
			                    
			                    ((MyVaadinUI)UI.getCurrent()).refreshMenuLayout();
			                    UI.getCurrent().getNavigator().navigateTo(CSV.NAME);
						 	}else{
						 		throw new Exception("Prueba admin/admin");
						 	}

					} catch (Exception e) {
						logger.debug("Mal login ", e );
						if (loginPanel.getComponentCount() > 2) {
	                        // Remove the previous error message
	                        loginPanel.removeComponent(loginPanel.getComponent(2));
	                    }
	                    // Add new error message
	                    Label error = new Label(
	                            e.getMessage(),
	                            ContentMode.HTML);
	                    error.addStyleName("error");
	                    error.setSizeUndefined();
	                    error.addStyleName("light");
	                    // Add animation
	                    error.addStyleName("v-animate-reveal");
	                    loginPanel.addComponent(error);
	                    username.focus();

					}
				 }
			}
		});
        signin.addShortcutListener(enter);
        
        loginPanel.addComponent(fields);

        //vl.
        addComponent(loginPanel);
        //vl.
        setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);

    }
     
	@Override
	public void enter(ViewChangeEvent event) {
		
		((MyVaadinUI)UI.getCurrent()).getMenuLayout().setVisible(false);
		
	}
}
