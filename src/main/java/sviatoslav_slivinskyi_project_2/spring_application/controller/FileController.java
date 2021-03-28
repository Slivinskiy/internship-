package sviatoslav_slivinskyi_project_2.spring_application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sviatoslav_slivinskyi_project_2.spring_application.model.File;
import sviatoslav_slivinskyi_project_2.spring_application.service.FileService;
import sviatoslav_slivinskyi_project_2.spring_application.service.UserService;

import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/files")
public class FileController {

    @Autowired
    FileService fileService;
    @Autowired
    UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @GetMapping
    public String viewFiles(Authentication authentication, Model model){
        String username = authentication.getName();
        List<File> files = fileService.findAllFiles(username);
        model.addAttribute("files", files);
        return "files";
    }

    @GetMapping("/deleteFile/{fileId}")
    public String deleteFile(File file, Model model){
        fileService.deleteFile(file.getFileId());
        if (fileService.getOptionalFile(file.getFileId()).isPresent()) {
            model.addAttribute("ErrorDeleteFile", true);
            LOGGER.error("An error occurs during deleting the file");
        } else {
            model.addAttribute("SuccessDeleteFile", true);
            LOGGER.info("The file was deleted");
        }
        return "result";
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity downloadFile(@PathVariable Long fileId){
        File file = fileService.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getFileData());
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, @ModelAttribute("file") File file, Model model, Authentication authentication) throws IOException {
        String username = authentication.getName();
        file.setUser(userService.getUserByName(username));
        file.setFileData(fileUpload.getBytes());
        file.setFileName(fileUpload.getOriginalFilename());
        file.setFileSize(String.valueOf(fileUpload.getSize()));
        file.setContentType(fileUpload.getContentType());
        if (fileService.uploadFile(file)==null){
            model.addAttribute("ErrorUploadFile", true);
            LOGGER.error("An error occurs during uploading the note");
        } else {
            model.addAttribute("SuccessUploadFile", true);
            LOGGER.info("The file was uploaded");
        }
        return "result";
    }

}
