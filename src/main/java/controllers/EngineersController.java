package controllers;

import db.DBHelper;
import models.*;
import models.Engineer;
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

public class EngineersController {
    public EngineersController() {
        this.setupEndpoints();
    }

    private void setupEndpoints(){

        get("/engineers", (req, res) -> {
            Map<String, Object> model = new HashMap();
            model.put("template", "templates/engineers/index.vtl");

            List<Engineer> engineers = DBHelper.getAll(Engineer.class);
            model.put("engineers", engineers);

            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());


        get("/engineers/new", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);
            model.put("template", "templates/engineers/create.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());


        post("/engineers", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            String firstName = req.queryParams("engineer-firstname");
            String lastName = req.queryParams("engineer-lastname");
            int salary = Integer.parseInt(req.queryParams("engineer-salary"));
            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);

            Engineer newEngineer = new Engineer(firstName, lastName, salary, department);
            DBHelper.save(newEngineer);

            res.redirect("/engineers");
            return null;
        }, new VelocityTemplateEngine());

        get("/engineers/edit/:id", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int engineerId = Integer.parseInt(req.params(":id"));
            Engineer foundEngineer = DBHelper.find(engineerId, Engineer.class);
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);
            model.put("foundEngineer", foundEngineer);
            model.put("template", "templates/engineers/edit.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/engineers/:id", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int engineerId = Integer.parseInt(req.params(":id"));
            Engineer foundEngineer = DBHelper.find(engineerId, Engineer.class);
            String firstName = req.queryParams("engineer-firstname");
            String lastName = req.queryParams("engineer-lastname");
            int salary = Integer.parseInt(req.queryParams("engineer-salary"));
            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);

            foundEngineer.setFirstName(firstName);
            foundEngineer.setLastName(lastName);
            foundEngineer.setSalary(salary);
            DBHelper.update(foundEngineer);

            res.redirect("/engineers");
            return null;
        }, new VelocityTemplateEngine());

        post("/engineers/delete/:id", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int engineerId = Integer.parseInt(req.params(":id"));
            Engineer engineerToDelete = DBHelper.find(engineerId, Engineer.class);
            DBHelper.delete(engineerToDelete);

            res.redirect("/engineers");
            return null;
        }, new VelocityTemplateEngine());
    }

}
