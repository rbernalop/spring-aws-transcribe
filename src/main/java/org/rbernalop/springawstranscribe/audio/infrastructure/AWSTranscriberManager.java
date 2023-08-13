package org.rbernalop.springawstranscribe.audio.infrastructure;

import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClientBuilder;
import com.amazonaws.services.transcribe.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.rbernalop.springawstranscribe.audio.infrastructure.util.RandomString;
import org.rbernalop.springawstranscribe.audio.infrastructure.dto.TranscriptionResponseDTO;
import org.rbernalop.springawstranscribe.audio.infrastructure.configuration.AWSConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class AWSTranscriberManager {
    private final AWSConfig awsConfig;

    AmazonTranscribe transcribeClient() {
        return AmazonTranscribeClientBuilder.standard().withCredentials(awsConfig.awsStaticCredentialsProvider())
                .withRegion(awsConfig.getRegion()).build();
    }

    public String makeTranscription(String key, String bucketUrl) {
        Media media = new Media().withMediaFileUri(bucketUrl);
        String jobName = key.concat(RandomString.make());
        StartTranscriptionJobRequest startTranscriptionJobRequest = new StartTranscriptionJobRequest()
                .withLanguageCode(LanguageCode.EnUS).withTranscriptionJobName(jobName).withMedia(media);
        StartTranscriptionJobResult startTranscriptionJobResult = transcribeClient()
                .startTranscriptionJob(startTranscriptionJobRequest);
        return startTranscriptionJobResult.getTranscriptionJob().getTranscriptionJobName();
    }

    public String getTranscriptFileUriByJobName(String jobName) {
        GetTranscriptionJobRequest getTranscriptionJobRequest =
            new GetTranscriptionJobRequest().withTranscriptionJobName(jobName);

        while (true) {
            GetTranscriptionJobResult getTranscriptionJobResult = transcribeClient().getTranscriptionJob(getTranscriptionJobRequest);
            TranscriptionJob transcriptionJob = getTranscriptionJobResult.getTranscriptionJob();
            String transcriptionJobStatus = transcriptionJob.getTranscriptionJobStatus();

            if (transcriptionJobStatus.equalsIgnoreCase(TranscriptionJobStatus.COMPLETED.name())) {
                return getTranscriptionJobResult.getTranscriptionJob().getTranscript().getTranscriptFileUri();
            } else if (transcriptionJobStatus.equalsIgnoreCase(TranscriptionJobStatus.FAILED.name())) {
                return null;
            } else if (transcriptionJobStatus.equalsIgnoreCase(TranscriptionJobStatus.IN_PROGRESS.name())) {
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public String getTranscriptionByJobName(String jobName) {
        String uri = getTranscriptFileUriByJobName(jobName);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
        Request request = new Request.Builder().url(uri).build();
        Response response;
        try {
            response = okHttpClient.newCall(request).execute();
            assert response.body() != null;
            String body = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            response.close();
            TranscriptionResponseDTO transcriptionResponse = objectMapper.readValue(body, TranscriptionResponseDTO.class);
            return transcriptionResponse.getResults().getTranscripts().get(0).getTranscript();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTranscriptionJobByName(String jobName) {
        DeleteTranscriptionJobRequest deleteTranscriptionJobRequest =
            new DeleteTranscriptionJobRequest().withTranscriptionJobName(jobName);
        transcribeClient().deleteTranscriptionJob(deleteTranscriptionJobRequest);
    }
}
