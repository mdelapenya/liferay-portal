package com.liferay.portal.portlet.container.tests;

import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import cucumber.runtime.arquillian.CukeSpace;
import cucumber.runtime.arquillian.api.Features;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import org.junit.runner.RunWith;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.*;

/**
 * @author Manuel de la Peña
 */
@Features("com/liferay/portal/portlet/container/features/users.feature")
@RunWith(CukeSpace.class)
public class UsersTest {

	@After
	public void tearDown() throws Exception {
		if (user != null) {
			UserLocalServiceUtil.deleteUser(user);
		}
	}

	@Deployment
	public static Archive<?> createDeployment() {
		return create(WebArchive.class)
			.addPackages(true, "com.liferay.portal.kernel.test")
			.addAsResource("test-portal-impl.properties")
			.addAsResource(
				"com/liferay/portal/portlet/container/features/users.feature");
	}

	@Inject
	private User user;

	@Given("^I have a user$")
	public void setUpUser() throws Exception {
		user = UserTestUtil.addUser();
	}

	@When("^I create the user$")
	public void createUser() throws Exception{
		user = UserTestUtil.addUser();
	}

	@Then("^The user can be retrieved by service layer$")
	public void shouldBeRetrievedByServiceLayer() throws Exception {
		User retrievedUser = UserLocalServiceUtil.getUser(user.getUserId());

		assertNotNull(retrievedUser);
		assertEquals(user.getUserId(), retrievedUser.getUserId());
	}

}