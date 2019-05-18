package pen.eco.voting

import pen.eco.planning.Block

/** The premise of a consumption proposal. */
interface Premise {}

/** A production council premise.
  * @param production The proposed production. */
class ProductionPremise (val production : Block) : Premise {}

/** A program consumption premise.
  * @param programId Id of the program. */
class ProgramPremise (val programId : Long) : Premise {}
