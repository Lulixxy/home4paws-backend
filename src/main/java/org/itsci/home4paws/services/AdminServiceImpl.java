package org.itsci.home4paws.services;

import org.itsci.home4paws.model.Admin;
import org.itsci.home4paws.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository ar;

    @Override
    public Admin doAdminlogin(String username, String password) {
        Admin admin = ar.findByUsername(username);
        if(admin != null && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }
}
