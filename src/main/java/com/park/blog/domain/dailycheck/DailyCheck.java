package com.park.blog.domain.dailycheck;

import java.util.List;
import lombok.Data;

@Data
public class DailyCheck {

    private Long dailyCheckId;
    private String dailyCheckName;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles;
}
