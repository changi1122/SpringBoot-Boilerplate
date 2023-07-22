package net.studio1122.boilerplate.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotEmpty
    private String title;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    @NotEmpty
    private String body;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false)
    private OffsetDateTime createdDate;

    private OffsetDateTime editedDate;

    private OffsetDateTime deletedDate;

    @Column(nullable = false)
    private Long view;

    @Column(nullable = false)
    private Long heart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public void setUser(User user) {
        this.user = user;

        if (user != null && !user.getPosts().contains(this)) {
            user.getPosts().add(this);
        }
    }

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Heart> hearts = new ArrayList<>();

    public void addHeart(Heart heart) {
        this.hearts.add(heart);

        if (heart.getPost() != this) {
            heart.setPost(this);
        }
    }
}
