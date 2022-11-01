package com.example.springbootstudy.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

@Service
public class FileService {

    @Value("${upload.path}")
    private String uploadPath;

    Predicate<MultipartFile> checkFileIsEmpty = file -> (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty());
    public String getFilenameForUserAvatar(MultipartFile file) throws IOException {

        if (checkFileIsEmpty.test(file)) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            return resultFilename;
        } else {
            return "default_user_avatar.png";
        }
    }

    public String getFilenameForMessagePicture(MultipartFile file) throws IOException {
        if (checkFileIsEmpty.test(file)) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            return resultFilename;
        } else {
            return null;
        }
    }

}
