package com.example.demo.study;


import com.example.demo.DemoApplication;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packagesOf = DemoApplication.class)
class ArchTests {

  @ArchTest
  ArchRule domainPackageRule = classes().that().resideInAPackage("..domain..")
          .should().onlyBeAccessed().byClassesThat()
          .resideInAnyPackage( "..member..", "..domain..");

  @ArchTest
  ArchRule memberPackageRule = noClasses().that().resideInAPackage("..domain..")
          .should().accessClassesThat().resideInAPackage("..member..");

  @ArchTest
  ArchRule studyPackageRule = noClasses().that().resideOutsideOfPackage("..study..")
          .should().accessClassesThat().resideInAPackage("..study..");

  @ArchTest
  ArchRule freeOfCycle = slices().matching("..demo.(*)..")
          .should().beFreeOfCycles();

  @Test
  void packageDependencyTests() {
    JavaClasses classes = new ClassFileImporter().importPackages("com.example.demo");

    /**
     * TODO ..domain.. 패키지에 있는 클래스는 ..study.., ..member.., domain에서 참조 가능.
     * TODO ..member.. 패키지에 있는 클래스는 ..study..와 ..member..에서만 참조가능
     * TODO (반대로) ..domain.. 패키지는 ..member.. 패키지를 참조하지 못한다.
     * TODO ..study.. 패키지에 있는 클래스는 ..study.. 에서만 참조가능.
     * TODO 순환 참조 없어야 한다.
     */


//            .resideInAnyPackage("..study..", "..member..", "..domain..");
//    domainPackageRule.check(classes);


//    memberPackageRule.check(classes);


//    studyPackageRule.check(classes);


//    freeOfCycle.check(classes);
  }

}
