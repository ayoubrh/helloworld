Feature: Test Hello World

  Scenario Outline: Formulaire Hello World et vérification
  	Given Ouverture du formulaire "http://localhost:8080/helloworld/app/"
    When Renseigner le champ avec la valeur <message>
    When Click sur le bouton envoyer
    Then verification du message : <message>

    Examples:
    | message   		| 
    | Test A			|
