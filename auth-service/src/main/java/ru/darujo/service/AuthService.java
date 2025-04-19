package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.darujo.model.Right;
import ru.darujo.model.Role;
import ru.darujo.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService implements UserDetailsService {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.loadUserByNikName(username);
        return new org.springframework.security.core.userdetails.User(user.getNikName(), user.getPassword(), mapGrandAuthority(user.getRoles(), user.getRights()));// нужно для спринга
    }

    private Collection<? extends GrantedAuthority> mapGrandAuthority(Collection<Role> roles, Collection<Right> rights) {
        Collection<SimpleGrantedAuthority> grantedAuthorities;
        Set<String> authority = new HashSet<>();
        roles.forEach(role -> {
            authority.add("ROLE_" + role.getName());
            role.getRights().forEach(right -> authority.add(right.getName()));
        });
        rights.forEach(right -> authority.add(right.getName()));
        grantedAuthorities = authority.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//        grantedAuthorities.addAll(rights.stream().map(right -> new SimpleGrantedAuthority(right.getName())).toList());
        return grantedAuthorities;
    }
}
