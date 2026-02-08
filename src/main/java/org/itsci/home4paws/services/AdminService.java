package org.itsci.home4paws.services;

import org.itsci.home4paws.model.Admin;

public interface AdminService {

    Admin doAdminlogin(String username, String password);
}
