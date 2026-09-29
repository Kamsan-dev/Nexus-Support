package com.kamsan.notificationservice.domain;

import com.kamsan.notificationservice.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Notification implements Serializable {

    private Event payload;
    private Map<String, String> headers;

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
