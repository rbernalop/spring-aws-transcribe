package org.rbernalop.springawstranscribe.audio.infrastructure.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.rbernalop.springawstranscribe.audio.application.transcribe.AudioTranscriber;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/audio")
public class AudioPostTranscribeController {
    private final AudioTranscriber audioTranscriber;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public AudioPostTranscribeResponse postAudio(@RequestParam("audioFile") MultipartFile audioFile) {
        String transcriptionText = audioTranscriber.transcribe(audioFile);
        return new AudioPostTranscribeResponse(transcriptionText);
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Getter
class AudioPostTranscribeResponse {
    private String transcriptionText;
}