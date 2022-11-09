package com.park.blog.Controller;

import com.park.blog.domain.dailycheck.DailyCheck;
import com.park.blog.domain.dailycheck.DailyCheckForm;
import com.park.blog.domain.dailycheck.UploadFile;
import com.park.blog.file.FileStore;
import com.park.blog.repository.DailyCheckRepository;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DailyCheckController {
    private final DailyCheckRepository dailyCheckRepository;
    //파일을 저장하기 위한 메서드들이 정의되어 있는 클래스
    private final FileStore fileStore;

    @GetMapping("/dailyCheck/add")
    public String newDailyCheck(@ModelAttribute DailyCheck dailyCheck) {
        return "dailyCheck/dailyCheck-form";
    }

    @PostMapping("/dailyCheck/add")
    public String addDailyCheck(@ModelAttribute DailyCheckForm dailyCheckForm,
                                RedirectAttributes redirectAttributes)
        throws IOException {
        //데이터베이스에 파일이름 저장
        DailyCheck dailyCheck = new DailyCheck();
        dailyCheck.setDailyCheckName(dailyCheckForm.getDailyCheckName());
        DailyCheck savedDailyCheck = dailyCheckRepository.saveDailyCheck(dailyCheck);
        dailyCheck.setDailyCheckId(savedDailyCheck.getDailyCheckId());
        log.info("dailyCheckId = {}",savedDailyCheck.getDailyCheckId());

        //로컬폴더에 파일저장
        UploadFile attachFile = fileStore.storeFile(dailyCheckForm.getAttachFile());
        List<UploadFile> storeImageFiles = fileStore.storeFiles(dailyCheckForm.getImageFiles());

        //데이터베이스에 저장 (파일 자체를 저장하지는 않고 파일 경로 정도만 저장한다.)
        //dailyCheckId는 AutoIncrement로 생성해서 repository에서 넣어주었다.
        dailyCheck.setAttachFile(attachFile);
        dailyCheck.setImageFiles(storeImageFiles);
        dailyCheckRepository.saveInLocal(dailyCheck);
        log.info("dailyCheck.getAttachFile() = {}",dailyCheck.getAttachFile());
        log.info("dailyCheck.getImageFiles()= {}",dailyCheck.getImageFiles());
        redirectAttributes.addAttribute("dailyCheckId",dailyCheck.getDailyCheckId());
        return "redirect:/dailyCheck/{dailyCheckId}";
    }

    @GetMapping("/dailyCheck/{dailyCheckId}")
    public String dailyChecks(@PathVariable Long dailyCheckId, Model model) {
        DailyCheck dailyCheck = dailyCheckRepository.findSaveInLocalById(dailyCheckId);
        log.info("dailyChecks = {}",dailyCheck.getAttachFile());
        log.info("dailyChecks = {}",dailyCheck.getImageFiles());
        model.addAttribute(dailyCheck);
        return "dailyCheck/dailyCheck-view";
    }

    //dailyCheck-form 갔다가 와서 @ResponseBody 랜더링 로직
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        log.info("FullPath = {}","file:" + fileStore.getFullPath(filename));
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @GetMapping("/attach/{dailyCheckId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long dailyCheckId)
        throws MalformedURLException {
        log.info("downloadAttach");
        DailyCheck dailyCheck = dailyCheckRepository.findById(dailyCheckId);
        String storeFileName = dailyCheck.getAttachFile().getStoreFileName();
        String uploadFileName = dailyCheck.getAttachFile().getUploadFileName();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        log.info("uploadFileName = {}",uploadFileName);

        String encodingUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + uploadFileName + "\"";

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition)
            .body(resource);
    }

}
