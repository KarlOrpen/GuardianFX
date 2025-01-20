package cz.korpen.guardianfx.controllers;

import cz.korpen.guardianfx.manager.CategoryManager;

import java.util.ResourceBundle;

public abstract class BaseController {

    protected ResourceBundle resources = ResourceBundle.getBundle("cz.korpen.guardianfx.messages");
    protected CategoryManager categoryManager = CategoryManager.getInstance();
}
