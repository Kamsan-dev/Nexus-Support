package com.kamsan.ticketservice.domain;

import com.kamsan.ticketservice.event.Event;
import com.kamsan.ticketservice.utils.UserUtils;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Map;

import static java.time.OffsetTime.now;

@Setter
@Getter
@NoArgsConstructor
public class Notification implements Serializable {

    private Event payload;
    private Map<String, String> headers;

    private Notification(@NotNull Event payload, @NotNull Map<String, String> headers){
        this.payload = payload;
        this.headers = headers;
    }

    public Notification(Event payload){
        this(payload, Map.of(
                MessageHeaders.ID, UserUtils.randomUUID.get().toString(),
                MessageHeaders.TIMESTAMP, now().toString()));
    }

    public boolean equals(@Nullable Object other) {
        // Using nullSafeEquals for proper array equals comparisons
        return (this == other || (other instanceof Notification that &&
                ObjectUtils.nullSafeEquals(this.payload, that.payload) && this.headers.equals(that.headers)));
    }

    public int hashCode() {
        // Using nullSafeHashCode for proper array hashCode handling
        return ObjectUtils.nullSafeHash(this.payload, this.headers);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(" [payload=");
        sb.append(this.payload);
        sb.append(", headers=").append(this.headers).append(']');
        return sb.toString();
    }
}
