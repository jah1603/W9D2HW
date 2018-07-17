package controllers;

import db.DBHelper;
import models.Department;
import models.Employee;
import models.Engineer;
import models.Manager;
import spark.ModelAndView;
import spark.ResponseTransformer;
import spark.TemplateEngine;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.post;

public class ManagersController {
    public ManagersController() {
        this.setupEndpoints();
    }

   private void setupEndpoints(){

        get("/managers", (req, res) -> {
            Map<String, Object> model = new HashMap();
            model.put("template", "templates/managers/index.vtl.html");

            List<Manager> managers = DBHelper.getAll(Manager.class);
            model.put("managers", managers);

            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

       get("/managers/new", (req, res) -> {
           HashMap<String, Object> model = new HashMap<>();
           List<Department> departments = DBHelper.getAll(Department.class);
           model.put("departments", departments);
           model.put("template", "templates/managers/create.vtl");
           return new ModelAndView(model, "templates/layout.vtl");
       }, new VelocityTemplateEngine());

       post("/managers", (req, res) -> {
           HashMap<String, Object> model = new HashMap<>();
           String firstName = req.queryParams("manager-firstname");
           String lastName = req.queryParams("manager-lastname");
           double budget = Double.parseDouble(req.queryParams("manager-budget"));
           int salary = Integer.parseInt(req.queryParams("manager-salary"));
           int departmentId = Integer.parseInt(req.queryParams("department"));
           Department department = DBHelper.find(departmentId, Department.class);

           Manager newManager = new Manager(firstName, lastName, salary, department, budget);
           DBHelper.save(newManager);

           res.redirect("/managers");
           return null;
       }, new VelocityTemplateEngine());

       get("/managers/edit/:id", (req, res) -> {
           HashMap<String, Object> model = new HashMap<>();
           int managerId = Integer.parseInt(req.params(":id"));
           Manager foundManager = DBHelper.find(managerId, Manager.class);
           List<Department> departments = DBHelper.getAll(Department.class);
           model.put("departments", departments);
           model.put("foundManager", foundManager);
           model.put("template", "templates/managers/edit.vtl");
           return new ModelAndView(model, "templates/layout.vtl");
       }, new VelocityTemplateEngine());

       post("/managers/:id", (req, res) -> {
           HashMap<String, Object> model = new HashMap<>();
           int managerId = Integer.parseInt(req.params(":id"));
           Manager foundManager = DBHelper.find(managerId, Manager.class);
           String firstName = req.queryParams("manager-firstname");
           String lastName = req.queryParams("manager-lastname");
           double budget = Double.parseDouble(req.queryParams("manager-budget"));
           int salary = Integer.parseInt(req.queryParams("manager-salary"));
           int departmentId = Integer.parseInt(req.queryParams("department"));
           Department department = DBHelper.find(departmentId, Department.class);

           foundManager.setFirstName(firstName);
           foundManager.setLastName(lastName);
           foundManager.setSalary(salary);
           foundManager.setBudget(budget);
           DBHelper.update(foundManager);

           res.redirect("/managers");
           return null;
       }, new VelocityTemplateEngine());

       post("/managers/delete/:id", (req, res) -> {
           HashMap<String, Object> model = new HashMap<>();
           int managerId = Integer.parseInt(req.params(":id"));
           Manager managerToDelete = DBHelper.find(managerId, Manager.class);
           DBHelper.delete(managerToDelete);

           res.redirect("/managers");
           return null;
       }, new VelocityTemplateEngine());
   }

}
