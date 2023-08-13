# Audio Transcription project using AWS Transcribe and Spring Boot

This project uses the AWS transcription service, called Amazon Transcribe, to convert audio files to text. It integrates with Spring Boot to provide an API that allows you to upload audio files to the AWS S3 service and then request the transcription of these files using an HTTP call.

## Settings ⚙️

Before running the project, make sure to set the following environment variables in your development environment:

- `AWS_ACCESS_KEY`: The AWS access key for authentication.
- `AWS_SECRET_KEY`: The AWS secret key for authentication.
- `AWS_REGION`: The AWS Region where the Transcribe and S3 service is located.
- `AWS_S3_BUCKET_NAME`: The name of the S3 bucket where the audio files will be stored.

## How to use the project 📚

1. Clone the project repository from GitHub: [Repository Link](https://github.com/rbernalop/spring-aws-transcribe).
2. Set the above mentioned environment variables on your system.
3. Open a terminal and navigate to the project directory.
4. Run the project using the `mvnw spring-boot:run` command.

## Upload an audio file and get the transcript 🎤

You can use the following curl request to upload an audio file to the S3 service and request the transcription:

```bash
curl --location 'http://localhost:8080/api/v1/audio' \
--form 'audioFile=@"local_file_dir"'
```

Replace local_file_dir with the full local path of the audio file you want to upload for transcription.

## Project workflow 📝
An HTTP request is received to upload an audio file to the /api/v1/audio endpoint.
1. The audio file is uploaded to the specified S3 bucket using the AWS credentials provided in the environment variables.
2. A transcription job is started in the Amazon Transcribe service, using the audio file stored in S3.
3. The Transcribe service processes the audio file and generates a textual transcription.
4. The result of the transcription is returned as an HTTP response to the client.
