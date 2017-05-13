package capstone.controller;

import capstone.entity.model.Report;
import capstone.entity.model.RolePermission;
import capstone.service.ReportService;
import capstone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Collection;

/**
 * Created by Adam Mandel, Derek Albano, Nina Tham, Phat Duong, and Ryan Harnett.
 * This class is a controller used for providing the GUI with the necessary methods to communicate  with and manipulate
 * the data about Reports in the backend
 */
@Controller
public class ReportController extends AuthenticateController {

    /**
     * The instance of the ReportService that contains the necessary methods to access and manipulate
     * Reports stored in the backend
     */
    private ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService, UserService userService){
        super(userService);
        this.reportService = reportService;
    }

    /**
     * Method to get the list of all Report objects from the backend
     * @return a list of all available Report objects and an OK HttpStatus.
     */
    @RequestMapping(value = "/api/reportList",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Report>> getReportList() {
        if(authenticate(RolePermission.MAINTAIN_REPORTS) || authenticate(RolePermission.GENERATE_REPORT)){
            return new ResponseEntity<>(reportService.getReportList(), HttpStatus.OK);
        } else if (authenticate(null)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Method to get the Report object with the specified id by the user from the backend
     * @param reportId the id of the Report that needs retrieving
     * @return the Report with the specified id, null if the Client with that id does not exist, with a OK HttpStatus
     */
    @RequestMapping(value = "/api/reportList/{reportId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Report> getReport(
            @PathVariable("reportId") int reportId) {
        if(authenticate(RolePermission.MAINTAIN_REPORTS) || authenticate(RolePermission.GENERATE_REPORT)){
            return new ResponseEntity<>(reportService.getReport(reportId), HttpStatus.OK);
        } else if (authenticate(null)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Method to send a message to the back end to add a new Report record to the database
     * @param report to be added
     * @return the added Report with a CREATED status. INTERNAL_SERVER_ERROR if there is any problem with the insertion
     */
    @RequestMapping(value = "/api/reportList",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Report> addReport(
            @RequestBody Report report) {
        if(authenticate(RolePermission.MAINTAIN_REPORTS) || authenticate(RolePermission.GENERATE_REPORT)){
            Report reportAdded = reportService.addReport(report);
            if(reportAdded != null){
                return new ResponseEntity<>(reportAdded, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (authenticate(null)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    /**
     * Method to send a message to the back end to update a Report record with a specified reportId in the database
     * @param reportId the id of the updating Report
     * @param report the updating Report that holds the updated data that will replace the old one
     * @return the updated Report with an OK HttpStatus, INTERNAL_SERVER_ERROR otherwise
     */
    @RequestMapping(value = "/api/reportList/{reportId}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Report> updateReport(
            @PathVariable("reportId") int reportId,
            @RequestBody Report report) {
        if(authenticate(RolePermission.MAINTAIN_REPORTS)){
            Report reportUpdated = null;
            if(report.getId() == reportId) {
                reportUpdated = reportService.updateReport(report);
            }
            if(reportUpdated != null){
                return new ResponseEntity<>(reportUpdated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (authenticate(null)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    /**
     * Method to send a message to the backend to delete a Report record with a specified reportId from the database
     * @param reportId the id of the Report that needs deleting
     * @return the deleted Report
     */
    @RequestMapping(value = "/api/reportList/{reportId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Report> deleteReport(
            @PathVariable("reportId") int reportId) {
        if(authenticate(RolePermission.MAINTAIN_REPORTS)){
            reportService.deleteReport(reportId);
            return new ResponseEntity<>(reportService.getReport(reportId), HttpStatus.OK);
        } else if (authenticate(null)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * This is just an example for serving up a pdf.
     * @return pdf
     */
    @RequestMapping(value = "/api/pdfReport",
            method = RequestMethod.GET,
            produces = "application/pdf")
    public ResponseEntity<String> getPdfReport() {
        try {
            return new ResponseEntity<>(Base64.getEncoder().encodeToString(Files.readAllBytes(new File("res/report.pdf").toPath())), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method to get the list of all Report objects from the backend
     * @return a list of all available Report objects and an OK HttpStatus.
     */
    @RequestMapping(value = "/api/generateReport/{reportId}",
            method = RequestMethod.GET,
            produces = "application/pdf"
            )
    public ResponseEntity<String> generateReport(
            @PathVariable("reportId") int reportId,
            @RequestParam(value = "selectionData[]", required = false) String[] selectionData) {
        byte[] result = reportService.generateReport(reportId, selectionData);
        return new ResponseEntity<>(Base64.getEncoder().encodeToString(result), HttpStatus.OK);
    }
}
