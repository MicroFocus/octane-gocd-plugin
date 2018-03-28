[![Codacy Badge](https://api.codacy.com/project/badge/Grade/96323a38b84b4fce981761d3f30b99ec)](https://www.codacy.com/app/HPSoftware/octane-gocd-plugin?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=MicroFocus/octane-gocd-plugin&amp;utm_campaign=Badge_Grade)

[![Build status](https://ci.appveyor.com/api/projects/status/y2e0msiuq88o0ddt?svg=true)](https://ci.appveyor.com/project/m-seldin/octane-gocd-plugin-isjhc)

# Micro Focus ALM Octane GoCD plugin 
This plugin integrates GoCD with ALM Octane, enabling ALM Octane to display GoCD pipelines, trigger pipeline runs, and track build and test run results, as well as committed changes. 

Note: You can connect your GoCD server to only one Space in ALM Octane. The same GoCD server cannot connect to multiple spaces or ALM Octane instances.

 ## Requirements 
This plugin requires GoCD version 17.9 or later.

Before you install the plugin, obtain an API Access Client ID and Client secret from your ALM Octane’s shared space admin.<br /> 
The plugin uses these for authentication when communicating with ALM.
 
 ## How to install 
Install this plugin on your GoCD server:

1. Download the plugin’s **.jar** file and store it on your GoCd server in `<go-server-directory>/plugins/external/`.
2. Restart your GoCD server.
3. Configure the plugin to connect to ALM Octane:
	1. In GoCD, open **Admin > Plugins**: 
You should see the **ALM Octane GoCD Plugin**. Click the cogwheel in front of it. 
	2. Enter the The URL of the ALM Octane server, using its fully qualified domain name (FQDN). <br />
	Use the following format (port is optional): `http://hostname:port/ui/?p=<SpaceID>`. 
Example:  `http://myServer.myCompany.com:8081/ui/?p=1002` <br />
**Tip:** You can copy the URL from the address bar of the browser in which you opened ALM Octane.
	3. Enter the API Access **Client ID** and **Client Secret** that the plugin should use to access ALM Octane. 
	4. Set the **GoCD API Username** and **GoCD API Password**. 
These are the credentials the plugin uses  to access the GoCD server. 
	5. When you save your settings, the plugin tests the connections to ALM Octane and to the GoCD server. If the connections succeed, the settings are saved, otherwise you are notified of the problem. 

4. In ALM Octane, add your GoCD server as a CI Server:  
In ALM Octane click the Settings cogwheel and select **Spaces**. (DevOps permissions are required) 
	1. Open the **DevOps** tab and select the CI Servers section. 
	2. Click + to add a new CI Server. 
	3. Select your GoCD server from the dropdown list, provide a name for the CI server and save. 
	4. With the GoCD server is added to the list in the CI servers section. You can see its name, instance ID, server type, URL, connection status and SDK version. 
 
 You can now add GoCD pipelines in ALM Octane. 
 
 ## Connecting GoCD pipelines to ALM Octane 
 This GoCD plugin provides ALM Octane with information about pipeline runs and enables triggering a pipeline run from ALM Octane. 

The plugin notifies ALM Octane when a pipeline run starts, sending also an estimated run duration.

When a pipeline run ends, the plugin sends ALM Octane the build duration, build stability, test results and SCM changes. 

In ALM Octane, you can trigger a pipeline run or track and analyse the pipeline run information provided by the plugin. 
 
 ## Test run results 
Make sure to declare your xml-report-files as artifacts or your build. 

This enables the plugin to locate the test results, convert them to a format that the ALM Octane recognizes, and send the test results to ALM Octane.

The plugin supports only test results in the following formats: 
* JUnit 
* NUnit 2.5 
* NUnit 3.0 


## Contribute to the GOCD plugin
* Contributions of code are always welcome!
* Follow the standard GIT workflow: Fork, Code, Commit, Push and start a Pull request
* Each pull request will be tested, pass static code analysis and code review results.
* All efforts will be made to expedite this process.
### Guidelines
* Document your code – it enables others to continue the great work you did on the code and update it.

Feel free to contact us on any question related to contributions - `octane.ci.plugins@gmail.com`

