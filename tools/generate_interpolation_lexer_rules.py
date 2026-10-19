#!/usr/bin/env python3
"""Refresh interpolation aliases after changing default-mode rules, then run mvn spotless:apply."""

from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1] / "src/main/java/org/rumbledb/parser"

for language in ("imports/Common", "jsoniq/Jsoniq", "xquery/XQuery"):
    path = ROOT / (language + "Lexer.g4")
    source = path.read_text()
    if language.startswith("imports/"):
        source = re.split(r"(?m)^INTERPOLATION_[A-Za-z_0-9]+\s*\n", source)[0].rstrip()
    else:
        source = re.split(r"(?m)^mode INTERPOLATION_MODE;", source)[0].rstrip()
    default_mode = re.split(r"(?m)^mode ", source)[0]
    declarations = re.sub(r"/\*.*?\*/", "", default_mode, flags=re.S)
    declarations = re.sub(r"(?m)^\s*//[^\n]*", "", declarations)
    rules = list(re.finditer(r"(?m)^([A-Z][A-Za-z_0-9]*)\s*:", declarations))
    aliases = []
    for index, rule in enumerate(rules):
        name = rule.group(1)
        end = rules[index + 1].start() if index + 1 < len(rules) else len(declarations)
        body = declarations[rule.end():end]
        commands = re.findall(r"->\s*([A-Za-z][^;]*?)\s*;", body)
        # Referencing a lexer rule reuses its pattern, but not its channel/mode commands.
        command = commands[0] + ", " if commands else ""
        command += f"type ({name})"
        aliases.append(f"INTERPOLATION_{name}\n   : {name} -> {command}\n   ;")
    aliases = "\n\n".join(aliases)
    # Imported modes merge with the language-specific aliases in the same priority order as DEFAULT_MODE.
    mode = "" if language.startswith("imports/") else "mode INTERPOLATION_MODE;\n\n"
    path.write_text(source + "\n\n" + mode + aliases + "\n")
