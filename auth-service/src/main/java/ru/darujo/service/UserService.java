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
import ru.darujo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByNikName(String name) {
        return userRepository.findByNikNameIgnoreCase(name);
    }

    @Transactional
    public User loadUserByNikName(String nikName) throws UsernameNotFoundException {
        return findByNikName(nikName).orElseThrow(() -> new UsernameNotFoundException("Не наден пользователь по логину"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = loadUserByNikName(username);
        return new org.springframework.security.core.userdetails.User(user.getNikName(), user.getUserpasword(), mapGrandAuthority(user.getRoles(), user.getRights()));// нужно для спринга
    }

    private Collection<? extends GrantedAuthority> mapGrandAuthority(Collection<Role> roles, Collection<Right> rigths) {
        Collection<SimpleGrantedAuthority> grantedAuthorities;
        Set<String> authority = new HashSet<>();
        roles.forEach(role -> {
            authority.add("ROLE_" + role.getName());
            role.getRights().forEach(right -> authority.add(right.getName()));
        });
        rigths.forEach(right -> authority.add(right.getName()));
        grantedAuthorities = authority.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//        grantedAuthorities.addAll(rigths.stream().map(right -> new SimpleGrantedAuthority(right.getName())).toList());
        return grantedAuthorities;
    }
}
