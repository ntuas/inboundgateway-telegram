package com.nt.ntuas.inboundgateway.telegram.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@ConfigurationProperties("inboundgateway.backend.product")
@Validated
public class ProductBackendProperties {

    @NotBlank
    private String queue;

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductBackendProperties)) return false;
        ProductBackendProperties that = (ProductBackendProperties) o;
        return Objects.equals(queue, that.queue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queue);
    }

    @Override
    public String toString() {
        return "ProductBackendProperties{" +
                "queue='" + queue + '\'' +
                '}';
    }
}
