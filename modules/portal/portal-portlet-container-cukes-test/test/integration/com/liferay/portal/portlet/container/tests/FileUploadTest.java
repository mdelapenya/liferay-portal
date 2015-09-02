package com.liferay.portal.portlet.container.tests;

import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import cucumber.runtime.arquillian.CukeSpace;
import cucumber.runtime.arquillian.api.Features;

import java.io.InputStream;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

import org.junit.Assert;
import org.junit.runner.RunWith;

/**
 * @author Manuel de la Peña
 */
@Features("com/liferay/portal/portlet/container/features/file-upload.feature")
@RunWith(CukeSpace.class)
public class FileUploadTest {

	public String portalUrl = "http://localhost:8080";

	@Given("^I have a group, a user with permissions in the group, and a file$")
	public void setUpScenario() throws Exception {
		_group = GroupTestUtil.addGroup();

		_user = UserTestUtil.addGroupAdminUser(_group);

		_fileName = RandomTestUtil.randomString(15);
	}

	@When("^I upload a file with user' credentials$")
	public void uploadFile() throws Exception{
		InputStream inputStream = getClass().getResourceAsStream(
			"/com/liferay/portal/portlet/container/dependencies/" +
				"file_upload.txt");

		HttpResponse httpResponse = HttpRequest
			.post(portalUrl.toString() + _ADD_FILE_JSONWS_URL)
			.form("cmd", "{\"/dlapp/add-file-entry\":{}}")
			.form("repositoryId", _group.getGroupId())
			.form("folderId", 0)
			.form("sourceFileName", "file_upload.txt")
			.form("mimeType", "plain/text")
			.form("title", _fileName)
			.form("description", RandomTestUtil.randomString())
			.form("changeLog", "v1.0")
			.form("file", inputStream)
			.basicAuthentication(
				_user.getEmailAddress(), _user.getPasswordUnencrypted())
			.send();

		int statusCode = httpResponse.statusCode();

		Assert.assertEquals(200, statusCode);
	}

	@Then("^the file is stored in the site$")
	public void shouldRetrieveTheFileFromTheGroup() throws Exception {
		try {
			Assert.assertNotNull(
				DLAppLocalServiceUtil.getFileEntry(
					_group.getGroupId(), 0, _fileName));
		}
		finally {
			if (_user != null) {
				UserLocalServiceUtil.deleteUser(_user);
			}

			if (_group != null) {
				GroupLocalServiceUtil.deleteGroup(_group);
			}
		}
	}

	private String _fileName;
	private Group _group;
	private User _user;

	private static final String _ADD_FILE_JSONWS_URL =
		"/api/jsonws/invoke";

}