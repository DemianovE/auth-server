/**
 * Package holding all inwards ports of the system.
 * <p>
 *     The ports here are mainly the use-cases of the system.
 *     For the use-cases the following structure is used:
 *     <ul>
 *         <li>Use-case port: the port itself, with {@code #execute()}
 *              entry point;</li>
 *         <li>Critical handler: handler which can be used to perform
 *              pre-process checks. The throw error from this handler
 *              will return the {@code Failed} state automaticaly;</li>
 *         <li>Dispatcher: can be used to modify the output for
 *              specific purposes;</li>
 *     </ul>
 * </p>
 * @since 0.1.0-alpha
 */
package org.demianov.auth.main.core.application.ports.in;
