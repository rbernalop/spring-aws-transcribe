package org.rbernalop.springawstranscribe.audio.domain.port;

import org.springframework.web.multipart.MultipartFile;

public interface AudioToTextTransformer {
    String transcribe(MultipartFile audioFile);
}
