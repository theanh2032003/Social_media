package com.example.demo.model;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name="users")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullname;

    @Column(unique = true, nullable = false)
    private String textId;

    private String avatar;

    private String banner;

    private String bio;

    private String location;

    private boolean locked=false;

    private boolean enable = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "user")
    Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "user")
    Set<UserFriend> friends = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        List<SimpleGrantedAuthority> authorities = roles.stream().map(i-> new SimpleGrantedAuthority(i.getName().toString()))
                .collect(Collectors.toList());

        return authorities;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }

    public User(String email, String password, String fullname, String textId, Set<Role> roles, Set<Comment> comments, Set<Post> posts) {
        super();
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.roles = roles;
        this.comments = comments;
        this.posts = posts;
        this.friends = null;
        this.textId = textId;
        this.avatar = "/public/default/nullAvatar.jpg";
        this.banner = "/public/default/defaultBanner.jpg";
        this.bio ="";
        this.location="";
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return this.password;
    }



}

