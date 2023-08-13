package org.rbernalop.springawstranscribe.audio.application.transcribe;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rbernalop.springawstranscribe.audio.domain.port.AudioToTextTransformer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@AllArgsConstructor
public class AudioTranscriber {
    private final AudioToTextTransformer audioToTextTransformer;

    public String transcribe(MultipartFile audioFile) {
        return audioToTextTransformer.transcribe(audioFile);
    }
}
