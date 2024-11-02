import { LitElement, html, css } from 'lit';
import '@vaadin/text-field';
import '@vaadin/button';
import '@vaadin/checkbox';
import '@vaadin/details';
import '@vaadin/vertical-layout';
import '@vaadin/icon';
import '@vaadin/icons';
import { Notification } from '@vaadin/notification';
import { JsonRpc } from 'jsonrpc';
import { projectDir, templateName, templateNamespace, backstageUrl, remoteUrl, giteaSharedNetworkUrl, giteaUsername, giteaPassword, remoteName, remoteBranch } from 'build-time-data';

export class QwcTemplate extends LitElement {

  jsonRpc = new JsonRpc(this);

  static styles = css`
    :host {
      margin: 20px;
      display: flex;
      flex-direction: column;
      align-items: left;
      justify-content: center;
      font-family: Arial, sans-serif;
    }
    div {
      background-color: #f9f9f9;
      padding: 20px;
      border-radius: 5px;
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }
    label {
      font-weight: bold;
    }
    .large-input {
      width: 500px;
    }
    button {
      padding: 10px 20px;
      border: none;
      border-radius: 5px;
      background-color: #007BFF;
      color: white;
      cursor: pointer;
    }
    button:hover {
      background-color: #0056b3;
    }
    button:disabled {
      background-color: #cccccc;
      cursor: not-allowed;
    }
    button:disabled:hover {
      background-color: #cccccc;
    }
    h2 {
      color: #333;
    }
    a {
      color: #007BFF;
      text-decoration: none;
    }
    a:hover {
      text-decoration: underline;
    }

    .hide {
      visibility: hidden;
    }
    .show {
      visibility: visible;
    }
  `;

  static properties = {
    projectDir: { state: true },
    templateName: { state: true },
    templateNamespace: { state: true },
    remoteUrl: { state: true },
    giteaSharedNetworkUrl: { state: true },
    giteaUsername: { state: true },
    giteaPassword: { state: true },
    remoteName: { state: true },
    remoteBranch: { state: true },
    excludePaths: { state: true },
    backstageUrl: { state: true },
    commit: { state: true },
    push: { state: true },
    notificationMessage: { state: true },
    notificationOpened: { state: true },
  };

  constructor() {
    super();
    this.projectDir = projectDir;
    this.templateName = templateName;
    this.templateNamespace = templateNamespace;
    this.backstageUrl = backstageUrl;
    this.commit = false;
    this.push = false;
    this.remoteUrl = remoteUrl;
    this.giteaSharedNetworkUrl = giteaSharedNetworkUrl;
    this.giteaUsername = giteaUsername;
    this.giteaPassword = giteaPassword;
    this.remoteName = remoteName;
    this.remoteBranch = remoteBranch;
    this.excludePaths = [];
    this.newPath = '';
    this.notificationMessage = '';
    this.notificationOpened = false;
  }

  render() {
    return html`
      <vaadin-horizontal-layout>
        <div>
          <vaadin-vertical-layout>
            <vaadin-text-field 
              label="Template Name" 
              placeholder="Enter template name"
              .value="${this.templateName}" 
              @input="${(e) => this.templateName = e.target.value}">
            </vaadin-text-field>

            <vaadin-text-field 
              label="Template Namespace" 
              placeholder="Enter template namespace"
              .value="${this.templateNamespace}" 
              @input="${(e) => this.templateNamespace = e.target.value}">
            </vaadin-text-field>

            <vaadin-text-field 
              label="Project Path" 
              placeholder="Enter the project path"
              .value="${this.projectDir}" 
              @input="${(e) => this.projectDir = e.target.value}">
            </vaadin-text-field>

            <details title="Exclude paths">
              <summary>Exclude Paths</summary>
              ${this.excludePaths.map((path, index) => html`
                <div class="path-item">
                  <span>${path}</span>
                  <vaadin-button theme="error" @click="${() => this._removePath(index)}">Remove</vaadin-button>
                </div>
              `)}

              <vaadin-text-field 
                placeholder="Add a path to exclude" 
                .value="${this.newPath}" 
                @input="${(e) => this.newPath = e.target.value}">
              </vaadin-text-field>

              <vaadin-button @click="${this._addPath}">Add Path</vaadin-button>
            </details>

            <vaadin-button theme="primary" @click="${this._generate}">
              Generate
            </vaadin-button>
          </vaadin-vertical-layout>
        </div>

        <div>
          <vaadin-vertical-layout>
            <vaadin-text-field 
              label="Backstage URL" 
              .value="${this.backstageUrl}" 
              @input="${(e) => this.backstageUrl = e.target.value}">
            </vaadin-text-field>

            <vaadin-text-field 
              label="Branch to commit" 
              .value="${this.remoteBranch}" 
              @input="${(e) => this.remoteBranch = e.target.value}">
            </vaadin-text-field>

            <vaadin-checkbox 
              .checked="${this.commit}" 
              @change="${this._handleCheckboxChange('commit')}">
              Commit Changes
            </vaadin-checkbox>

            <vaadin-text-field 
              label="Remote URL" 
              .value="${this.remoteUrl}" 
              @input="${(e) => this.remoteUrl = e.target.value}">
            </vaadin-text-field>


            <vaadin-text-field 
              label="Remote Name" 
              .value="${this.remoteName}" 
              @input="${(e) => this.remoteName = e.target.value}">
            </vaadin-text-field>

            <vaadin-checkbox 
              .checked="${this.push}" 
              @change="${this._handleCheckboxChange('push')}">
              Push Changes
            </vaadin-checkbox>

            <vaadin-button 
              theme="primary" 
              ?disabled="${!(this.commit && this.push)}"
              @click="${this._install}">
              Install
            </vaadin-button>
          </vaadin-vertical-layout>
        </div>
      </vaadin-horizontal-layout>
    `;
  }

  _addPath() {
    if (this.newPath.trim()) {
      this.excludePaths = [...this.excludePaths, this.newPath.trim()];
      this.newPath = '';
    }
  }

  _removePath(index) {
    this.excludePaths = this.excludePaths.filter((_, i) => i !== index);
  }

  _generate() {
    this.jsonRpc.generateTemplate().then(() => {
      this._showNotification('Template generated successfully!', true);
    }).catch((err) => {
      this._showNotification(`Generation failed: ${err.message}`, false);
    });
  }

  _install() {
    this.jsonRpc.install({
      path: this.projectDir, 
      name: this.templateName, 
      remoteUrl: this.remoteUrl, 
      giteaSharedNetworkUrl: this.giteaSharedNetworkUrl,
      giteaUsername: this.giteaUsername,
      giteaPassword: this.giteaPassword,
      remoteName: this.remoteName, 
      remoteBranch: this.remoteBranch, 
      commit: this.commit, 
      push: this.push
    }).then(() => {
      this._showNotification('Template installed successfully!', true);
    }).catch((err) => {
      this._showNotification(`Installation failed: ${err.message}`, false);
    });
  }

  _showNotification(message, success) {
    Notification.show(message, {
      position: 'top-end',
      duration: 4000,
      theme: success ? 'success' : 'error',
    });
  }

  _handleCheckboxChange(field) {
    return (event) => {
      this[field] = event.target.checked;
      this.requestUpdate();
    };
  }
}

customElements.define('qwc-template', QwcTemplate);
