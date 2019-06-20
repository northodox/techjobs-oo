package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, @RequestParam int id) {

        //get the Job with the given ID and pass it into the view
        Job job = jobData.findById(id);
        model.addAttribute("job", job);
        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors) {

        // Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.
        if(!errors.hasErrors()) {

            Employer empl = jobData.getEmployers().findById(jobForm.getEmployerId());
            Location locale = jobData.getLocations().findById(jobForm.getLocationId());
            PositionType positype = jobData.getPositionTypes().findById(jobForm.getPositionTypeId());
            CoreCompetency corecomp = jobData.getCoreCompetencies().findById(jobForm.getCoreCompetencyId());

            Job newJob = new Job(jobForm.getName(), empl, locale, positype, corecomp);
            jobData.add(newJob);

            return "redirect:?id=" + newJob.getId();
        }
        else{
            return "new-job";
        }
    }
}
