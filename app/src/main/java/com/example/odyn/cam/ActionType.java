package com.example.odyn.cam;

/*
	RecType to rodzaj nagrania/zdjęcia z kamery.

	ActionType to co należy zrobić z kamerą.

*/

/**
 * Jest to typ wyliczeniowy odpowiadający za działania związane z kamerą.
 */
public enum ActionType {
	stop,	// wyłącz nagrywanie
	start,	// włącz nagrywanie albo zrób zdjęcie
	toggle	// przełącz nagrywa/nienagrywa
}
