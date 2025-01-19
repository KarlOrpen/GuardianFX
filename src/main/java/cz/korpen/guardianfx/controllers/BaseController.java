package cz.korpen.guardianfx.controllers;

import cz.korpen.guardianfx.manager.CategoryManager;

public abstract class BaseController {

    protected CategoryManager categoryManager = CategoryManager.getInstance();
}
