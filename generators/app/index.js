"use strict";
const Generator = require("yeoman-generator");

module.exports = class extends Generator {
  async prompting() {
    const prompts = [
      {
        type: "input",
        name: "name",
        message: "Project name",
        default: this.appname
      },
      {
        type: "input",
        name: "title",
        message: "Project friendly name (for documentation)",
        default: this.appname
      },
      {
        type: "input",
        name: "description",
        message: "Project description (for documentation)",
        default: this.appname
      },
      {
        type: "input",
        name: "package",
        message: "Base package",
        default: "net.robyf.ms"
      },
      {
        type: "input",
        name: "starterVersion",
        message: "Starter version",
        default: "Akaa.SR1-SNAPSHOT"
      }
    ];

    this.props = await this.prompt(prompts);
  }

  writing() {
    this.fs.copy(
      this.templatePath(".gitattributes"),
      this.destinationPath(".gitattributes")
    );
    this.fs.copy(
      this.templatePath(".gitignore"),
      this.destinationPath(".gitignore")
    );
    this.fs.copy(
      this.templatePath("gradle/**"),
      this.destinationPath("gradle")
    );
    this.fs.copy(this.templatePath("gradlew"), this.destinationPath("gradlew"));
    this.fs.copy(
      this.templatePath("gradlew.bat"),
      this.destinationPath("gradlew.bat")
    );
    this.fs.copy(
      this.templatePath("gradle.properties"),
      this.destinationPath("gradle.properties")
    );
    this.fs.copyTpl(
      this.templatePath("settings.gradle"),
      this.destinationPath("settings.gradle"),
      { name: this.props.name }
    );
    this.fs.copyTpl(
      this.templatePath("build.gradle"),
      this.destinationPath("build.gradle"),
      { name: this.props.name, starterVersion: this.props.starterVersion }
    );
  }

  install() {
    // this.installDependencies();
  }
};
