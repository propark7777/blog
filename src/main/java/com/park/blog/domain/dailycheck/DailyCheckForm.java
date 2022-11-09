package com.park.blog.domain.dailycheck;

import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DailyCheckForm {

    private Long dailyCheckId;
    private String dailyCheckName;
    private MultipartFile attachFile;
    private List<MultipartFile> imageFiles;
}
