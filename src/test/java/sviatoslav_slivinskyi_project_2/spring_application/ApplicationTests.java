package sviatoslav_slivinskyi_project_2.spring_application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sviatoslav_slivinskyi_project_2.spring_application.model.Credential;
import sviatoslav_slivinskyi_project_2.spring_application.model.File;
import sviatoslav_slivinskyi_project_2.spring_application.model.Note;
import sviatoslav_slivinskyi_project_2.spring_application.model.User;
import sviatoslav_slivinskyi_project_2.spring_application.service.CredentialService;
import sviatoslav_slivinskyi_project_2.spring_application.service.FileService;
import sviatoslav_slivinskyi_project_2.spring_application.service.NoteService;
import sviatoslav_slivinskyi_project_2.spring_application.service.UserService;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ApplicationTests {

	@Autowired
	UserService userService;
	@Autowired
	NoteService noteService;
	@Autowired
	FileService fileService;
	@Autowired
	CredentialService credentialService;


	@Test
	void contextLoads() {
	}

	@Test
	@Order(1)
	void saveUserTest(){
		User user = new User();
		user.setFirstName("Andriy");
		user.setLastName("Khalak");
		user.setUsername("halk");
		user.setPassWord("halk95");

		User user1 = userService.insertUser(user);

		assertNotNull(user1);
		assertNotEquals(user.getPassword(), user1.getPassword());
		assertEquals(user1.getUsername(), "halk");
		assertTrue(user1.getUserId() > 0);
	}

	@Test
	@Order(2)
	void getUserByUserNameTest(){
		User user = userService.getUserByName("halk");
		assertNotNull(user);
		assertEquals(user.getFirstName(), "Andriy");
		assertTrue(user.getUserId() > 0);
	}

	@Test
	@Order(3)
	void saveGetUpdateDeleteNoteTest(){
		Note note = new Note();
		note.setNoteTitle("Note for testing");
		note.setNoteDescription("Hope that all tests will past");

		Note note1 = noteService.saveNote(note);
		assertNotNull(note1);
		assertTrue(note1.getNoteId() > 0);
		assertEquals(note1.getNoteTitle(), "Note for testing");
		assertEquals(note1.getNoteDescription(), note.getNoteDescription());

		Note note2 = noteService.getNote(note1.getNoteId());
		assertNotNull(note2);
		assertEquals(note2.getNoteTitle(), note1.getNoteTitle());

		note2.setNoteTitle("note to update");
		int updateResult = noteService.updateNote(note2);
		assertTrue(updateResult > 0);
		assertNotEquals(note2.getNoteTitle(), note1.getNoteTitle());

		Long noteId = note2.getNoteId();
		noteService.deleteNote(noteId);
		assertEquals(noteService.getOptionalNote(noteId), Optional.empty());
	}

	@Test
	@Order(4)
	void uploadDownloadDeleteFileTest(){
		File file = new File();
		file.setFileName("Test File");
		file.setContentType("Content Type");

		File file1 = fileService.uploadFile(file);
		assertNotNull(file1);
		assertEquals(file.getFileName(), file1.getFileName());
		assertEquals(file1.getContentType(), "Content Type");
		assertTrue(file1.getFileId() > 0);

		File file2 = fileService.getFile(file1.getFileId());
		assertNotNull(file2);
		assertTrue(file2.getFileId() > 0);
		assertEquals(file2.getContentType(), file1.getContentType());
		assertEquals(file2.getFileName(), "Test File");

		Long fileId = file2.getFileId();
		fileService.deleteFile(fileId);
		assertEquals(fileService.getOptionalFile(fileId), Optional.empty());
	}

	@Test
	@Order(5)
	void saveGetUpdateDeleteCredentials(){
		Credential credential = new Credential();
		credential.setUrl("youtube.com");
		credential.setUsername("test");
		credential.setPassword("test1");

		Credential credential1 = credentialService.saveCredentials(credential);
		assertNotEquals(credential.getPassword(), credential1.getPassword());
		assertNotNull(credential1);
		assertTrue(credential1.getCredentialId() > 0);
		assertEquals(credential1.getUrl(), credential.getUrl());
		assertEquals(credential1.getUsername(), "test");

		Credential credential2 = credentialService.getCredentialsById(credential1.getCredentialId());
		assertNotNull(credential2);
		assertTrue(credential2.getCredentialId() > 0);
		assertEquals(credential1.getUsername(), credential2.getUsername());

		credential2.setUsername("updated Username");
		int result = credentialService.updateCredentials(credential2);
		assertTrue(result > 0);
		assertNotEquals(credential1.getUsername(), credential2.getUsername());

		Long credentialsId = credential2.getCredentialId();
		credentialService.deleteCredentials(credentialsId);
		assertEquals(credentialService.getOptionalCredentials(credentialsId), Optional.empty());
	}
}
