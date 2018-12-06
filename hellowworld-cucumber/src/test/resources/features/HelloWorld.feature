Feature: Test Hello World

  @Test
  Scenario Outline: Formulaire Hello World et vérification
  	Given Ouverture du formulaire "http://localhost:8080/helloworld/app/"
    When Renseigner le champ avec la valeur <message>
    When Click sur le bouton envoyer
    Then verification du message : <message>

    Examples:
    | message   		| 
    | Test A			|
    | Test B			|
    | Test C			|
    | Test D			|
    | Test E			|
    | Test F			|
    | Test G			|