package ru.darujo.service;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.Project;
import ru.darujo.model.User;

import java.util.Collection;
import java.util.HashSet;

@Service
public class AuthService implements UserDetailsService {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userService.loadUserByNikName(username);

        if (user.isBlock()) {
            throw new ResourceNotFoundRunTime("Пользователь заблокирован");
        }

        return new org.springframework.security.core.userdetails.User(user.getNikName(), user.getPassword(),
                mapGrandAuthority()
        );// нужно для спринга
    }

    public @NonNull User getUser(@NonNull String username) throws UsernameNotFoundException {
        return userService.loadUserByNikName(username);
    }

    private Collection<? extends GrantedAuthority> mapGrandAuthority() {
        Collection<SimpleGrantedAuthority> grantedAuthorities;
        grantedAuthorities = new HashSet<>();
//        grantedAuthorities.addAll(rights.stream().map(right -> new SimpleGrantedAuthority(right.getName())).toList());
        return grantedAuthorities;
    }

    public @NonNull User changeProject(@NonNull String username, Long projectId) throws UsernameNotFoundException {
        User user = userService.loadUserByNikName(username);
        if (user.isBlock()) {
            throw new ResourceNotFoundRunTime("Пользователь заблокирован");
        }
        Project project = ProjectService.getInstance().findById(projectId);
        if (!user.getProjects().contains(project)) {
            throw new ResourceNotFoundRunTime("У вас нет доступа к проекту");
        }
        user.setCurrentProject(project);
        user = userService.saveUser(user);
        return user;
    }
}
