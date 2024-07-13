const core = require('@actions/core');
const fetch = require('node-fetch');

async function run() {
  try {
    const jiraUrl = core.getInput('jira-url');
    const jiraUsername = core.getInput('jira-username');
    const jiraApiToken = core.getInput('jira-api-token');
    const jqlQuery = core.getInput('jql-query');

    const response = await fetch(`${jiraUrl}/rest/api/2/search?jql=${encodeURIComponent(jqlQuery)}`, {
      method: 'GET',
      headers: {
        'Authorization': `Basic ${Buffer.from(`${jiraUsername}:${jiraApiToken}`).toString('base64')}`,
        'Accept': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error(`Error fetching data from Jira: ${response.statusText}`);
    }

    const data = await response.json();
    core.setOutput('response', JSON.stringify(data));

  } catch (error) {
    core.setFailed(`Action failed with error: ${error.message}`);
  }
}

run();
