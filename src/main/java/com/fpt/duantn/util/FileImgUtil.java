package com.fpt.duantn.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

@Component
public class FileImgUtil {
    public Blob convertMultipartFileToBlob(MultipartFile file) throws IOException, SQLException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        byte[] bytes = file.getBytes();
        return new SerialBlob(bytes);
    }
    public  byte[] convertBlobToByteArray(Blob blob) throws SQLException, IOException {
        if (blob == null) {
            return null;
        }
        try (InputStream inputStream = blob.getBinaryStream()) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }
}
