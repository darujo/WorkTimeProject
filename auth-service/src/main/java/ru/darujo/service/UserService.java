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
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository =userRepository;
    }

    public Optional<User> findByUserName(String name){
        return userRepository.findByUsername(name);
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("Не наден пользователь по имени"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getUserpasword(),mapGrandAuthority(user.getRoles(),user.getRights()));
    }
    private Collection<? extends GrantedAuthority> mapGrandAuthority(Collection<Role> roles, Collection<Right> rigths){
        Collection<SimpleGrantedAuthority> grantedAuthorities;
        grantedAuthorities =       roles.stream().map(role  -> new SimpleGrantedAuthority(role.getName() )).collect(Collectors.toList());
        grantedAuthorities.addAll(rigths.stream().map(right -> new SimpleGrantedAuthority(right.getName())).toList());
        return grantedAuthorities;
    }
}
