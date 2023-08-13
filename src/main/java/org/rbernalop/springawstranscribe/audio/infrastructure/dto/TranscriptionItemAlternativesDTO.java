package org.rbernalop.springawstranscribe.audio.infrastructure.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TranscriptionItemAlternativesDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String confidence;

    private String content;
}