package com.checkmarx.sdk.service.scanner;

import com.checkmarx.sdk.dto.sast.Filter;
import com.checkmarx.sdk.dto.ScanResults;
import com.checkmarx.sdk.dto.cx.*;
import com.checkmarx.sdk.dto.cx.xml.CxXMLResultsType;
import com.checkmarx.sdk.dto.filtering.FilterConfiguration;
import com.checkmarx.sdk.exception.CheckmarxException;

import org.json.JSONObject;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Class used to orchestrate submitting scans and retrieving results
 */
public interface CxClient extends ILegacyClient {

    /**
     * Get the last scan Id of a given project Id
     * @param projectId project Id
     * @return ID of the current scan
     */
    public Integer getLastScanId(Integer projectId);

    /**
     * Fetches scan data based on given scan identifier, as a {@link JSONObject}.
     *
     * @param scanId scan ID to use
     * @return  populated {@link JSONObject} if scan data was fetched; empty otherwise.
     */
    public JSONObject getScanData(String scanId);

    /**
     * Fetches the Timestamp of the last full scan
     *
     * @param projectId
     * @return Timestamp for when scan started.
     */
    public LocalDateTime getLastScanDate(Integer projectId);

    /**
     * Get the status of a given scanId
     *
     * @param scanId
     * @return Status code for current scan ID.
     */
    Integer getScanStatus(Integer scanId);

    /**
     * Generate a scan report request (xml) based on ScanId
     *
     * @param scanId
     * @return ID for scan report
     */
    Integer createScanReport(Integer scanId);

    /**
     * Get the status of a report being generated by reportId
     *
     * @param reportId
     * @return Status of current report
     */
    Integer getReportStatus(Integer reportId) throws CheckmarxException;


    /**
     * Retrieve the report by reportId, mapped to ScanResults DTO, applying filtering as requested
     *
     * @param reportId
     * @param filter
     * @return Contents of the current report
     * @throws CheckmarxException
     */
    public ScanResults getReportContent(Integer reportId, FilterConfiguration filter) throws CheckmarxException;

    /**
     * Retrieve the xml report by reportId, mapped to ScanResults DTO, applying filtering as requested
     *
     * @param reportId
     * @return
     * @throws CheckmarxException
     */
    public CxXMLResultsType getXmlReportContent(Integer reportId) throws CheckmarxException;


    /**
     * Returns custom field values read from a Checkmarx project, based on given projectId.
     *
     * @param projectId ID of project to lookup from Checkmarx
     * @return Map of custom field names to values
     */
    public Map<String, String> getCustomFields(Integer projectId);

    /**
     * Parse CX report file, mapped to ScanResults DTO, applying filtering as requested
     *
     * @param file
     * @param filter
     * @return Contents of the current report
     * @throws CheckmarxException
     */
    public ScanResults getReportContent(File file, FilterConfiguration filter) throws CheckmarxException;

    /**
     * @param vulnsFile
     * @param libsFile
     * @param filter
     * @return Contents of current OSA report
     * @throws CheckmarxException
     */
    public ScanResults getOsaReportContent(File vulnsFile, File libsFile, List<Filter> filter) throws CheckmarxException;


    public String getIssueDescription(Long scanId, Long pathId);

    /**
     * Create Project under a given Owner Id team
     *
     * @param ownerId
     * @param name
     * @return Id of the new project
     */
    public Integer createProject(String ownerId, String name);

    /**
     * Delete a project by Id
     * @param projectId
     */
    public void deleteProject(Integer projectId);

    /**
     * Delete a project by Id and, optionally, any related running scans
     * <p/>
     * If the project to be deleted has any running scans, the project will <b>not</b> be deleted unless
     * {@code deleteRunningScans} is {@code true}.
     *
     * @param projectId Id of the existing project to delete
     * @param deleteRunningScans if {@code true}, delete any in-progress scans for the project before deleting the project
     */
    public void deleteProject(Integer projectId, boolean deleteRunningScans);

    /**
     * Branch an existing project
     *
     * @param projectId Id of the existing project to branch
     * @param name Name for the new branched project
     * @return Id of the new branched project, or -1 if branch request was unsuccessful
     */
    public Integer branchProject(Integer projectId, String name);

    /**
     * Get All Projects in Checkmarx
     *
     * @return List of projects on server
     */
    public List<CxProject> getProjects() throws CheckmarxException;

    /**
     * Get All Projects in Checkmarx
     *
     * @return List of projects on server
     */
    public List<CxProject> getProjects(String teamId) throws CheckmarxException;

    /**
     * Get All Projects under a specific team within Checkmarx
     * <p>
     * using TeamId does not work.
     *
     * @param ownerId
     * @return ID of project
     */

    public Integer getProjectId(String ownerId, String name);

    /**
     * Return Project based on projectId
     *
     * @return Project data
     */
    public CxProject getProject(Integer projectId);


    /**
     * Check if a scan exists for a projectId
     *
     * @param projectId
     * @return true if scan exists
     */
    public boolean scanExists(Integer projectId);

    /**
     * Get scanId of existing if a scan exists for a projectId
     *
     * @param projectId
     * @return
     */
    public Integer getScanIdOfExistingScanIfExists(Integer projectId);

    /**
     * Create Scan Settings
     *
     * @param projectId
     * @param presetId
     * @param engineConfigId
     * @param postBackId
     * @param emailNotifications
     * @return ID for scan settings
     */
    public Integer createScanSetting(Integer projectId, Integer presetId, Integer engineConfigId,
                                     Integer postBackId, CxEmailNotifications emailNotifications);

    /**
     * Get Scan Settings for an existing project (JSON String)
     *
     * @param projectId
     * @return Current scan configuration
     */
    public String getScanSetting(Integer projectId);

    CxScanSettings getScanSettingsDto(int projectId);






    /**
     * Set Repository details for a project
     *
     * @param projectId
     * @param gitUrl
     * @param branch
     * @throws CheckmarxException
     */
    public void setProjectRepositoryDetails(Integer projectId, String gitUrl, String branch) throws CheckmarxException;

    /**
     * Update details for a project
     *
     * @param project
     * @throws CheckmarxException
     */
    public void updateProjectDetails(CxProject project) throws CheckmarxException;


    /**
     * Upload file (zip of source) for a project
     *
     * @param projectId
     * @param file
     * @throws CheckmarxException
     */
    public void uploadProjectSource(Integer projectId, File file) throws CheckmarxException;

    /**
     *
     * @param projectId Id of Checkmarx Project
     * @param excludeFolders list of folder exclusions to apply to a scan
     * @param excludeFiles list of file exclusions to apply to a scan
     */
    public void setProjectExcludeDetails(Integer projectId, List<String> excludeFolders, List<String> excludeFiles);

    /**
     *
     * @param ldapServerId
     * @param ldapGroupDn
     * @throws CheckmarxException
     */
    public Integer getLdapTeamMapId(Integer ldapServerId, String teamId, String ldapGroupDn) throws CheckmarxException;

    /**
     * Get teamId for given path
     *
     * @param teamPath
     * @return ID for team
     * @throws CheckmarxException
     */
    public String getTeamId(String teamPath) throws CheckmarxException;

    /**
     * Fetches all teams
     *
     * @return  a List containing the Teams in Checkmarx; versions prior to 9.0 return full name and ID only
     * @throws CheckmarxException
     */
    public List<CxTeam> getTeams() throws CheckmarxException;

    /**
     * Adds an LDAP team association - uses SOAP Web Service
     * @param ldapServerId
     * @param teamId
     * @param teamName
     * @param ldapGroupDn
     * @throws CheckmarxException
     */
    public void mapTeamLdap(Integer ldapServerId, String teamId, String teamName, String ldapGroupDn) throws CheckmarxException;

    /**
     * Retrieve LDAP team mapping associations
     * @param ldapServerId
     * @throws CheckmarxException
     */
    public List<CxTeamLdap> getTeamLdap(Integer ldapServerId) throws CheckmarxException;

    /**
     * Removes an LDAP team association - uses SOAP Web Service
     *
     * @param ldapServerId
     * @param teamId
     * @param teamName can be null/empty if using 9.0.  Only applicable to 8.x
     * @param ldapGroupDn
     * @throws CheckmarxException
     */
    public void removeTeamLdap(Integer ldapServerId, String teamId, String teamName, String ldapGroupDn) throws CheckmarxException;

    /**
     * Returns a list of roles in Checkmarx
     * @return List of roles on server
     */
    public List<CxRole> getRoles() throws CheckmarxException;

    /**
     * Returns the Id of an associated role in Checkmarx
     * @param roleName
     */
    public Integer getRoleId(String roleName) throws CheckmarxException;

    /**
     * Retrieve list of Role LDAP mappings associated with an LDAP server Id
     * @param ldapServerId
     * @return List of ldap roles associated with server
     * @throws CheckmarxException
     */
    public List<CxRoleLdap> getRoleLdap(Integer ldapServerId) throws CheckmarxException;

    /**
     * Retrieve the Id of a role mapping associated with an LDAP Group DN
     * @param ldapServerId
     * @param ldapGroupDn
     * @return Mapping between Cx and LDAP roles
     * @throws CheckmarxException
     */
    public Integer getLdapRoleMapId(Integer ldapServerId, Integer roleId, String ldapGroupDn) throws CheckmarxException;

    /**
     * @param ldapServerId
     * @param roleId
     * @param ldapGroupDn
     * @throws CheckmarxException
     */
    public void mapRoleLdap(Integer ldapServerId, Integer roleId, String ldapGroupDn) throws CheckmarxException;

    /**
     * Removes a role/ldap mapping association
     * @param roleMapId
     * @throws CheckmarxException
     */
    public void removeRoleLdap(Integer roleMapId) throws CheckmarxException;

    /**
     * Removes a role/ldap mapping association
     * @param ldapServerId
     * @param roleId
     * @param ldapGroupDn
     * @throws CheckmarxException
     */
    public void removeRoleLdap(Integer ldapServerId, Integer roleId, String ldapGroupDn) throws CheckmarxException;

    /**
     * Adds an LDAP team association - uses SOAP Web Service
     * @param ldapServerId
     * @param teamId
     * @param teamName
     * @param ldapGroupDn
     * @throws CheckmarxException
     */
    public void mapTeamLdapWS(Integer ldapServerId, String teamId, String teamName, String ldapGroupDn) throws CheckmarxException;

    /**
     * Removes an LDAP team association - uses SOAP Web Service
     *
      * @param ldapServerId
     * @param teamId
     * @param teamName
     * @param ldapGroupDn
     * @throws CheckmarxException
     */
    public void removeTeamLdapWS(Integer ldapServerId, String teamId, String teamName, String ldapGroupDn) throws CheckmarxException;


    /**
     * Create team under given parentId
     *
     * @param parentTeamId
     * @param teamName
     * @return new TeamId
     * @throws CheckmarxException
     */
    public String createTeamWS(String parentTeamId, String teamName) throws CheckmarxException;

//    /**
//     * Create team under given parentId - Will use REST API to create team for version 9.0+
//     *
//     * @param parentTeamId
//     * @param teamName
//     * @return new TeamId
//     * @throws CheckmarxException
//     */
//    public String createTeam(String parentTeamId, String teamName) throws CheckmarxException;

    /**
     * Move team to under given newParentId - Will use REST API to create team for version 9.0+
     *
     * @param teamId - Id of the team to be moved
     * @param newParentTeamId - id of the new parent team
     * @throws CheckmarxException
     */
    public void moveTeam(String teamId, String newParentTeamId) throws CheckmarxException;

    /**
     * Rename team (path is unaffected; only the actual name)
     *
     * @param teamId - Id of the team to be renamed
     * @param newTeamName - new team name
     * @throws CheckmarxException
     */
    public void renameTeam(String teamId, String newTeamName) throws CheckmarxException;

    /**
     * Create team under given parentId - Will use REST API to create team for version 9.0+
     *
     * @param teamId
     * @throws CheckmarxException
     */
    public void deleteTeam(String teamId) throws CheckmarxException;

    /**
     * Get a team Id based on the name and the Parent Team Id
     * @param parentTeamId
     * @param teamName
     * @return ID of team
     * @throws CheckmarxException
     */
    public String getTeamId(String parentTeamId, String teamName) throws CheckmarxException;


        /**
         * Delete a team based on the name for a given parent team id
         *
         * @param teamId
         * @throws CheckmarxException
         */
    public void deleteTeamWS(String teamId) throws CheckmarxException;

    /**
     * Get scan configuration Id by name.
     *
     * @param configuration
     * @return Scan configuration ID
     * @throws CheckmarxException
     */
    public Integer getScanConfiguration(String configuration) throws CheckmarxException;

//    /**
//     * Get scan configuration name by Id.
//     */
//    String getScanConfigurationName(int configurationId);

    /**
     * Fetch the Id of a given preset name
     *
     * @param preset name of the preset to find the Id for
     * @return Id for the scan configuration
     * @throws CheckmarxException
     */
    public Integer getPresetId(String preset) throws CheckmarxException;

    /**
     * Get scan summary for given scanId
     *
     * @param scanId
     * @return Id for the preset
     * @throws CheckmarxException
     */
    public CxScanSummary getScanSummaryByScanId(Integer scanId) throws CheckmarxException;

    /**
     * Get scan summary for the latest scan of a given project Id
     *
     * @param projectId project Id to retrieve the latest scan summary for
     * @return CxScanSummary containing scan summary information
     * @throws CheckmarxException
     */
    public CxScanSummary getScanSummary(Integer projectId) throws CheckmarxException;

    /**
     * Get scan summary for the latest scan associated with a teamName & projectName
     *
     * @param teamName
     * @param projectName
     * @return CxScanSummary containing scan summary information
     * @throws CheckmarxException
     */
    public CxScanSummary getScanSummary(String teamName, String projectName) throws CheckmarxException;

    /**
     * Create a scan based on the CxScanParams and return the scan Id
     *
     * @param params attributes used to define the project
     * @param comment
     * @return Scan Id associated with the new scan
     * @throws CheckmarxException
     */
    public Integer createScan(CxScanParams params, String comment) throws CheckmarxException;

    /**
     * Wait for the scan of a given scan Id to finish
     *
     * @param scanId
     * @throws CheckmarxException
     */
    public void waitForScanCompletion(Integer scanId) throws CheckmarxException;

    /**
     *
     * @param scanId
     * @return
     * @throws CheckmarxException
     */
    public void deleteScan(Integer scanId) throws CheckmarxException;

    /**
     *
     * @param scanId
     * @throws CheckmarxException
     */
    public void cancelScan(Integer scanId) throws CheckmarxException;

    /**
     * Create a scan based on the CxScanParams and wait for the scan to complete, returning the result XML Jaxb object
     *
     * @param params attributes used to define the project
     * @param comment
     * @return CxXMLResultType (Jaxb/XML object representation of the scan results)
     * @throws CheckmarxException
     */
    public CxXMLResultsType createScanAndReport(CxScanParams params, String comment) throws CheckmarxException;

    /**
     * Create a scan based on the CxScanParams and return the ScanResults object based on filters
     * @param params attributes used to define the project
     * @param comment
     * @param filters filters to apply to the scan result set (severity, category, cwe)
     * @return
     * @throws CheckmarxException
     */
    public ScanResults createScanAndReport(CxScanParams params, String comment, FilterConfiguration filters) throws CheckmarxException;

    /**
     * Create a scan based on the CxScanParams and wait for the scan to complete, returning the result XML Jaxb object
     *
     * @param teamName
     * @param projectName
     * @return
     * @throws CheckmarxException
     */
    public CxXMLResultsType getLatestScanReport(String teamName, String projectName) throws CheckmarxException;

    /**
     * Create a scan based on the CxScanParams and return the ScanResults object based on filters
     *
     * @param teamName
     * @param projectName
     * @param filters
     * @return Scan results
     * @throws CheckmarxException
     */
    public ScanResults getLatestScanResults(String teamName, String projectName, FilterConfiguration filters) throws CheckmarxException;

    public Integer getLdapServerId(String serverName) throws  CheckmarxException;

    //TODO Engine Management

}
