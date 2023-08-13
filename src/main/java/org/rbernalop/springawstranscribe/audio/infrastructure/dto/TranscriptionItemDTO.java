package org.rbernalop.springawstranscribe.audio.infrastructure.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TranscriptionItemDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String start_time;

    private String end_time;

    private List<TranscriptionItemAlternativesDTO> alternatives;

    private String type;
}
