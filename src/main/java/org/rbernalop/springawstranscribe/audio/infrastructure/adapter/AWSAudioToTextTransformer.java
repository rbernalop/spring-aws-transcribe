package org.rbernalop.springawstranscribe.audio.infrastructure.adapter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rbernalop.springawstranscribe.audio.domain.port.AudioToTextTransformer;
import org.rbernalop.springawstranscribe.audio.infrastructure.AWSTranscriberManager;
import org.rbernalop.springawstranscribe.audio.infrastructure.S3BucketManager;
import org.rbernalop.springawstranscribe.audio.infrastructure.configuration.S3BucketConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Slf4j
public class AWSAudioToTextTransformer implements AudioToTextTransformer {
    private final S3BucketManager s3BucketManager;
    private final AWSTranscriberManager awsTranscriberManager;
    private final S3BucketConfig s3BucketConfig;

    @Override
    public String transcribe(MultipartFile audioFile) {
        log.debug("Request to extract Speech Text from : {}", audioFile);

        String fileId = s3BucketManager.getFileId(audioFile);
        s3BucketManager.uploadFile(audioFile, s3BucketConfig.getName());

        String bucketUrl = s3BucketManager.getBucketUrl(fileId, s3BucketConfig.getName());

        String transcriptionJobName = awsTranscriberManager.makeTranscription(fileId, bucketUrl);
        String transcriptionResult = awsTranscriberManager.getTranscriptionByJobName(transcriptionJobName);

        s3BucketManager.deleteFile(fileId, s3BucketConfig.getName());
        awsTranscriberManager.deleteTranscriptionJobByName(transcriptionJobName);

        return transcriptionResult;
    }
}
