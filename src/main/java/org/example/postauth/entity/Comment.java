package org.example.postauth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String body;

    @ManyToOne
    private User user;

    @ManyToOne
    private Post post;

    @CreationTimestamp
    private LocalDateTime created_at;

    public String getCreated_at() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return created_at.toLocalTime().format(formatter);
    }
}
