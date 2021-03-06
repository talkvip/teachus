/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.frontend.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.cookies.CookieDefaults;
import org.apache.wicket.util.cookies.CookieUtils;

import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.Select2Enabler;
import dk.teachus.frontend.components.form.DefaultFocusBehavior;
import dk.teachus.frontend.components.menu.MenuItem;
import dk.teachus.frontend.components.menu.MenuItemPageLink;
import dk.teachus.frontend.utils.LocaleChoiceRenderer;

public abstract class UnAuthenticatedBasePage extends BasePage {
	private static final long serialVersionUID = 1L;
	
	public static final String USERNAME_PATH = "signInForm.username";
	public static final String PASSWORD_PATH = "signInForm.password";
	public static final String REMEMBER_PATH = "signInForm.remember";
	
	public static enum UnAuthenticatedPageCategory implements PageCategory {
		INFO
	}
	
	public static class User implements Serializable {
		private static final long serialVersionUID = 1L;

		private String username;

		private String password;

		public boolean isRemember() {
			return true;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
	}
	
	private Locale locale;
	
	private User user;
	
	public UnAuthenticatedBasePage() {
		createSignInBox();
		
		createLocaleBox();
	}

	private void createSignInBox() {
		add(new Label("signInLabel", TeachUsSession.get().getString("General.signIn"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		add(new FeedbackPanel("feedback"));
		
		user = new User();
		
		final Form<User> signInForm = new Form<User>("signInForm", new CompoundPropertyModel<User>(user)) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				signin();
			}
		};
		add(signInForm);
		
		final TextField<String> username = new TextField<String>("username"); //$NON-NLS-1$
		username.setOutputMarkupId(true);
		username.setRequired(true);
		username.add(new DefaultFocusBehavior());
		username.add(AttributeModifier.replace("placeholder", TeachUsSession.get().getString("General.username")));
		signInForm.add(username);
		
		final PasswordTextField password = new PasswordTextField("password");
		password.setResetPassword(false);
		password.add(AttributeModifier.replace("placeholder", TeachUsSession.get().getString("General.password")));
		signInForm.add(password);
		
		signInForm.add(new Button("signIn", new Model<String>("Log ind")));
	}
		
	private void signin() {		
		TeachUsSession teachUsSession = TeachUsSession.get();
		
		teachUsSession.signIn(user.getUsername(), user.getPassword());
		
		if (teachUsSession.isAuthenticated()) {			
			if (user.isRemember()) {
				CookieDefaults settings = new CookieDefaults();
				// 14 days
				settings.setMaxAge(60*60*24*14);
				CookieUtils cookieUtils = new CookieUtils(settings);
				cookieUtils.save("username", user.getUsername());
				cookieUtils.save("password", user.getPassword());
			}
			
			if (continueToOriginalDestination() == false) {
				throw new RestartResponseAtInterceptPageException(Application.get().getHomePage());
			}
		}
	}

	private void createLocaleBox() {
		add(new Label("localeLabel", TeachUsSession.get().getString("General.locale")));
		
		Form<Void> localeForm = new Form<Void>("localeForm") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return TeachUsSession.get().isAuthenticated() == false;
			}
		};
		add(localeForm);
		
		setLocale(TeachUsSession.get().getLocale());
		
		List<Locale> availableLocales = TeachUsApplication.get().getAvailableLocales();
		
		DropDownChoice<Locale> localeDropDown = new DropDownChoice<Locale>("locale", new PropertyModel<Locale>(this, "locale"), availableLocales, new LocaleChoiceRenderer());
		localeDropDown.add(new Select2Enabler());
		localeForm.add(localeDropDown);
		
		localeDropDown.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				TeachUsSession.get().changeLocale(getLocale());
				
				getRequestCycle().setResponsePage(UnAuthenticatedBasePage.this.getClass());
			}			
		});
	}

	@Override
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	@Override
	public List<MenuItem> getMenuItems() {
		List<MenuItem> menuItems = new ArrayList<MenuItem>();

		menuItems.add(new MenuItemPageLink(UnAuthenticatedPageCategory.INFO, TeachUsSession.get().getString("General.info"), InfoPage.class));
		
		return menuItems;
	}
	
	@Override
	public List<MenuItem> getRightMenuItems() {
		return null;
	}
	
	@Override
	public
	abstract UnAuthenticatedPageCategory getPageCategory();

}
