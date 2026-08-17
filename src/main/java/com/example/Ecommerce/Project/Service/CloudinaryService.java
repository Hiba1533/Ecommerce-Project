package com.example.Ecommerce.Project.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file) throws IOException {

        Map result = cloudinary.uploader().upload(
                file.getBytes(), //takes uploaded file and convert into raw bytes
                ObjectUtils.emptyMap()
        );

        return result.get("secure_url").toString();
    }

}

//take image from frontend
//upload to cloudinary
//get image url
//return url to product service